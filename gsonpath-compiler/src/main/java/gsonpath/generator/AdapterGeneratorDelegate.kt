package gsonpath.generator

import com.google.gson.JsonElement
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import gsonpath.model.GsonField
import gsonpath.model.GsonObject

import java.util.Arrays
import java.util.HashSet

class AdapterGeneratorDelegate {
    private val GSON_SUPPORTED_PRIMITIVE = HashSet(Arrays.asList(
            TypeName.BOOLEAN,
            TypeName.INT,
            TypeName.LONG,
            TypeName.DOUBLE
    ))

    private val CLASS_NAME_STRING = ClassName.get(String::class.java)
    private val CLASS_NAME_JSON_ELEMENT = ClassName.get(JsonElement::class.java)

    // Used to avoid naming conflicts.
    private var mCounterVariableCount: Int = 0

    @Throws(ProcessingException::class)
    fun addGsonAdapterReadCode(codeBlock: CodeBlock.Builder,
                               jsonMapping: GsonObject,
                               createModelAtBeginning: Boolean,
                               fieldAnnotationValidator: FieldAnnotationValidator?,
                               callback: ObjectParserCallback) {

        mCounterVariableCount = 0
        addGsonAdapterReadCodeInternal(codeBlock, jsonMapping, createModelAtBeginning, 0, fieldAnnotationValidator, callback)
    }

    @Throws(ProcessingException::class)
    private fun addGsonAdapterReadCodeInternal(codeBlock: CodeBlock.Builder,
                                               jsonMapping: GsonObject,
                                               createModelAtBeginning: Boolean,
                                               fieldDepth: Int,
                                               fieldAnnotationValidator: FieldAnnotationValidator?,
                                               callback: ObjectParserCallback) {

        val counterVariableName = "jsonFieldCounter" + mCounterVariableCount
        mCounterVariableCount++

        //
        // Ensure a Json object exists begin attempting to read it.
        //
        codeBlock.add("\n")
        codeBlock.add("// Ensure the object is not null.\n")
        codeBlock.beginControlFlow("if (!isValidValue(in))")

        if (fieldDepth == 0) {
            // Allow the calling method to inject different logic. Typically this would be to return.
            callback.onInitialObjectNull()

        } else {
            codeBlock.addStatement("break")
        }

        codeBlock.endControlFlow() // if

        // This is the first block of code to fire after the object is valid.
        if (fieldDepth == 0) {
            callback.onInitialise()
            codeBlock.add("\n")
        }

        if (jsonMapping.size() == 0) {
            return
        }

        if (jsonMapping.size() == 1) {
            val value = jsonMapping[jsonMapping.keySet().iterator().next()]

            if (value is GsonField) {
                val isDirectAccess = value.fieldInfo.isDirectAccess

                if (isDirectAccess) {
                    handleGsonField(value, codeBlock, createModelAtBeginning, fieldAnnotationValidator, callback)
                    return
                }
            }
        }

        codeBlock.addStatement("int \$L = 0", counterVariableName)
        codeBlock.addStatement("in.beginObject()")
        codeBlock.add("\n")
        codeBlock.beginControlFlow("while (in.hasNext())")

        //
        // Since all the required fields have been mapped, we can avoid calling 'nextName'.
        // This ends up yielding performance improvements on large datasets depending on
        // the ordering of the fields within the JSON.
        //
        codeBlock.beginControlFlow("if (\$L == \$L)", counterVariableName, jsonMapping.size())
        codeBlock.addStatement("in.skipValue()")
        codeBlock.addStatement("continue")
        codeBlock.endControlFlow() // if
        codeBlock.add("\n")

        codeBlock.beginControlFlow("switch (in.nextName())")

        var addBreak = true
        for (key in jsonMapping.keySet()) {
            codeBlock.add("case \"\$L\":\n", key)
            codeBlock.indent()

            // Increment the counter to ensure we track how many fields we have mapped.
            codeBlock.addStatement("\$L++", counterVariableName)

            val value = jsonMapping[key]
            if (value is GsonField) {
                handleGsonField(value, codeBlock, createModelAtBeginning, fieldAnnotationValidator, callback)

            } else {
                val nextLevelMap = value as GsonObject
                if (nextLevelMap.size() == 0) {
                    callback.onNodeEmpty()
                    addBreak = false
                } else {
                    addGsonAdapterReadCodeInternal(codeBlock, nextLevelMap, createModelAtBeginning,
                            fieldDepth + 1,
                            fieldAnnotationValidator,
                            callback)
                }
            }

            if (addBreak) {
                codeBlock.addStatement("break")
            }

            codeBlock.add("\n")
            codeBlock.unindent()
        }

        codeBlock.add("default:\n")
        codeBlock.indent()
        codeBlock.addStatement("in.skipValue()")
        codeBlock.addStatement("break")
        codeBlock.unindent()

        codeBlock.endControlFlow() // switch
        codeBlock.endControlFlow() // while
        codeBlock.add("\n")

        codeBlock.add("\n")

        codeBlock.addStatement("in.endObject()")
    }

    @Throws(ProcessingException::class)
    private fun handleGsonField(gsonField: GsonField, codeBlock: CodeBlock.Builder,
                                createModelAtBeginning: Boolean,
                                fieldAnnotationValidator: FieldAnnotationValidator?,
                                callback: ObjectParserCallback) {

        val fieldInfo = gsonField.fieldInfo

        // Make sure the field's annotations don't have any problems.
        fieldAnnotationValidator?.validateFieldAnnotations(fieldInfo)

        val fieldTypeName = fieldInfo.typeName

        // Add a new line to improve readability for the multi-lined mapping.
        codeBlock.add("\n")

        val variableName = gsonField.variableName
        var safeVariableName = variableName

        // A model isn't created if the constructor is called at the bottom of the type adapter.
        var checkIfResultIsNull = createModelAtBeginning
        if (gsonField.isRequired && !createModelAtBeginning) {
            safeVariableName += "_safe"
            checkIfResultIsNull = true
        }

        var callToString = false

        // If the field type is primitive, ensure that it is a supported primitive.
        if (fieldTypeName.isPrimitive && !GSON_SUPPORTED_PRIMITIVE.contains(fieldTypeName)) {
            throw ProcessingException("Unsupported primitive type found. Only boolean, int, double and long can be used.", fieldInfo.element)
        }

        if (GSON_SUPPORTED_CLASSES.contains(fieldTypeName.box())) {
            val fieldClassName = fieldTypeName.box() as ClassName

            // Special handling for strings.
            var handled = false
            if (fieldTypeName == CLASS_NAME_STRING) {
                val annotation = fieldInfo.getAnnotation(FlattenJson::class.java)
                if (annotation != null) {
                    handled = true

                    // FlattenJson is a special case. We always need to ensure that the JsonObject is not null.
                    if (!checkIfResultIsNull) {
                        safeVariableName += "_safe"
                        checkIfResultIsNull = true
                    }

                    codeBlock.addStatement("\$T \$L = mGson.getAdapter(\$T.class).read(in)",
                            CLASS_NAME_JSON_ELEMENT,
                            safeVariableName,
                            CLASS_NAME_JSON_ELEMENT)

                    callToString = true
                }
            }

            if (!handled) {
                val variableAssignment = String.format("%s = get%sSafely(in)",
                        safeVariableName,
                        fieldClassName.simpleName())

                if (checkIfResultIsNull) {
                    codeBlock.addStatement("\$L \$L", fieldClassName.simpleName(), variableAssignment)

                } else {
                    codeBlock.addStatement(variableAssignment)
                }
            }
        } else {
            val adapterName: String

            if (fieldTypeName is ParameterizedTypeName) {
                // This is a generic type
                adapterName = String.format("new com.google.gson.reflect.TypeToken<%s>(){}", fieldTypeName)

            } else {
                adapterName = fieldTypeName.toString() + ".class"
            }

            // Handle every other possible class by falling back onto the gson adapter.
            val variableAssignment = String.format("%s = mGson.getAdapter(%s).read(in)",
                    safeVariableName,
                    adapterName)

            if (checkIfResultIsNull) {
                codeBlock.addStatement("\$L \$L", fieldTypeName, variableAssignment)

            } else {
                codeBlock.addStatement(variableAssignment)
            }
        }

        if (checkIfResultIsNull) {
            val fieldName = fieldInfo.fieldName
            codeBlock.beginControlFlow("if (\$L != null)", safeVariableName)

            val assignmentBlock: String
            if (createModelAtBeginning) {
                assignmentBlock = "result." + fieldName
            } else {
                assignmentBlock = variableName
            }

            codeBlock.addStatement("\$L = \$L\$L",
                    assignmentBlock,
                    safeVariableName,
                    if (callToString) ".toString()" else "")

            // Inform the callback in case it wishes to add any further code.
            callback.onFieldAssigned(fieldName)

            if (gsonField.isRequired) {
                codeBlock.nextControlFlow("else")
                codeBlock.addStatement("throw new gsonpath.JsonFieldMissingException(\"Mandatory " + "JSON element '\$L' was null for class '\$L'\")",
                        gsonField.jsonPath,
                        fieldInfo.parentClassName)
            }

            codeBlock.endControlFlow() // if
        }
    }

    fun generateClassName(className: ClassName, classNameSuffix: String): String {
        //
        // We need to ensure that nested classes are have include their parent class as part of the name.
        // Otherwise this could cause file name contention when other nested classes have the same name
        //
        var fileName = ""
        for (name in className.simpleNames()) {
            fileName += name + "_"
        }

        // Make sure no '.' managed to sneak through!
        return fileName.replace(".", "_") + classNameSuffix
    }

    interface ObjectParserCallback {
        fun onInitialObjectNull()

        fun onInitialise()

        fun onFieldAssigned(fieldName: String)

        fun onNodeEmpty()
    }

    interface FieldAnnotationValidator {
        @Throws(ProcessingException::class)
        fun validateFieldAnnotations(fieldInfo: FieldInfo)
    }

    companion object {
        val GSON_SUPPORTED_CLASSES: Set<TypeName> = HashSet(Arrays.asList(
                TypeName.get(Boolean::class.java).box(),
                TypeName.get(Int::class.java).box(),
                TypeName.get(Long::class.java).box(),
                TypeName.get(Double::class.java).box(),
                TypeName.get(String::class.java).box()
        ))
    }
}
