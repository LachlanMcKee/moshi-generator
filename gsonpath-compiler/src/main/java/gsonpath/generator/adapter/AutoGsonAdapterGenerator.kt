package gsonpath.generator.adapter

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.*
import gsonpath.generator.*
import gsonpath.model.*

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import java.io.IOException
import java.util.*

class AutoGsonAdapterGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {
    private val GSON_SUPPORTED_PRIMITIVE = HashSet(Arrays.asList(
            TypeName.BOOLEAN,
            TypeName.INT,
            TypeName.LONG,
            TypeName.DOUBLE
    ))

    private val GSON_SUPPORTED_CLASSES: Set<TypeName> = HashSet(Arrays.asList(
            TypeName.get(Boolean::class.java).box(),
            TypeName.get(Int::class.java).box(),
            TypeName.get(Long::class.java).box(),
            TypeName.get(Double::class.java).box(),
            TypeName.get(String::class.java).box()
    ))

    private val CLASS_NAME_STRING = ClassName.get(String::class.java)
    private val CLASS_NAME_JSON_ELEMENT = ClassName.get(JsonElement::class.java)

    private val adapterGeneratorUtils = AdapterGeneratorUtils()

    // Used to avoid naming conflicts.
    private var mSafeWriteVariableCount: Int = 0
    private var mCounterVariableCount: Int = 0

    @Throws(ProcessingException::class)
    fun handle(modelElement: TypeElement): HandleResult {
        val modelClassName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(modelClassName.packageName(),
                adapterGeneratorUtils.generateClassName(modelClassName, "GsonTypeAdapter"))

        val adapterTypeBuilder = TypeSpec.classBuilder(adapterClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), modelClassName))
                .addField(Gson::class.java, "mGson", Modifier.PRIVATE, Modifier.FINAL)

        // Add the constructor which takes a gson instance for future use.
        adapterTypeBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Gson::class.java, "gson")
                .addStatement("this.\$N = \$N", "mGson", "gson")
                .build())

        val autoGsonAnnotation = modelElement.getAnnotation(AutoGsonAdapter::class.java)

        val concreteClassName: ClassName
        val fieldInfoList: List<FieldInfo>
        val isModelInterface = modelElement.kind.isInterface

        val properties = AutoGsonAdapterPropertiesFactory().create(
                autoGsonAnnotation, getDefaultsAnnotation(autoGsonAnnotation), isModelInterface)

        val fieldInfoFactory = FieldInfoFactory(processingEnv)
        if (!isModelInterface) {
            concreteClassName = modelClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(modelElement, properties.fieldsRequireAnnotation)

        } else {
            val interfaceInfo = ModelInterfaceGenerator(processingEnv).handle(modelElement)
            concreteClassName = interfaceInfo.parentClassName

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo)
        }

        val rootGsonObject = GsonObjectTreeFactory().createGsonObject(fieldInfoList, properties.rootField,
                properties.flattenDelimiter, properties.gsonFieldNamingPolicy, properties.gsonFieldValidationType,
                properties.pathSubstitutions)

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        val mandatoryInfoMap = MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootGsonObject)
        if (mandatoryInfoMap.isNotEmpty()) {
            mandatoryInfoMap.values
                    .mapIndexed { mandatoryIndex, mandatoryField ->
                        FieldSpec.builder(TypeName.INT, mandatoryField.indexVariableName)
                                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                .initializer("" + mandatoryIndex)
                                .build()
                    }
                    .forEach { adapterTypeBuilder.addField(it) }

            adapterTypeBuilder.addField(FieldSpec.builder(TypeName.INT, "MANDATORY_FIELDS_SIZE")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("" + mandatoryInfoMap.size)
                    .build())
        }

        adapterTypeBuilder.addMethod(createReadMethod(modelClassName, concreteClassName, mandatoryInfoMap, rootGsonObject))

        if (!isModelInterface) {
            adapterTypeBuilder.addMethod(createWriteMethod(modelClassName, rootGsonObject, properties.serializeNulls))

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            val writeMethod = MethodSpec.methodBuilder("write")
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(JsonWriter::class.java, "out")
                    .addParameter(modelClassName, "value")
                    .addException(IOException::class.java)

            adapterTypeBuilder.addMethod(writeMethod.build())
        }

        if (writeFile(adapterClassName.packageName(), adapterTypeBuilder)) {
            return HandleResult(modelClassName, adapterClassName)
        }

        throw ProcessingException("Failed to write generated file: " + adapterClassName.simpleName())
    }

    /**
     * public ImageSizes read(JsonReader in) throws IOException {
     */
    @Throws(ProcessingException::class)
    private fun createReadMethod(baseElement: ClassName,
                                 concreteElement: ClassName,
                                 mandatoryInfoMap: Map<String, MandatoryFieldInfo>,
                                 rootElements: GsonObject): MethodSpec {

        // Create a flat list of the variables and ensure they are ordered by their original field index within the POJO
        val flattenedFields = GsonObjectTreeFactory().getFlattenedFieldsFromGsonObject(rootElements)

        val readMethod = MethodSpec.methodBuilder("read")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(baseElement)
                .addParameter(JsonReader::class.java, "in")
                .addException(IOException::class.java)

        val codeBlock = CodeBlock.builder()
        val createModelAtBeginning = baseElement == concreteElement

        addGsonAdapterReadCode(codeBlock, rootElements, createModelAtBeginning,
                mandatoryInfoMap, concreteElement, flattenedFields)

        // If we have any mandatory fields, we now check if any values have been missed. If they are, it will raise an exception here.
        if (mandatoryInfoMap.isNotEmpty()) {
            codeBlock.add("\n// Mandatory object validation\n")
            codeBlock.beginControlFlow("for (int mandatoryFieldIndex = 0; " + "mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++)")

            codeBlock.add("\n// Check if a mandatory value is missing.\n")
            codeBlock.beginControlFlow("if (!mandatoryFieldsCheckList[mandatoryFieldIndex])")

            // The code must figure out the correct field name to insert into the error message.
            codeBlock.add("\n// Find the field name of the missing json value.\n")
            codeBlock.addStatement("String fieldName = null")
            codeBlock.beginControlFlow("switch (mandatoryFieldIndex)")

            for ((key, mandatoryFieldInfo) in mandatoryInfoMap) {
                codeBlock.add("case \$L:\n", mandatoryFieldInfo.indexVariableName)
                codeBlock.indent()
                codeBlock.addStatement("fieldName = \"\$L\"", mandatoryFieldInfo.gsonField.jsonPath)
                codeBlock.addStatement("break")
                codeBlock.unindent()
                codeBlock.add("\n")
            }

            codeBlock.endControlFlow() // Switch
            codeBlock.addStatement("throw new gsonpath.JsonFieldMissingException(\"Mandatory JSON " + "element '\" + fieldName + \"' was not found for class '\$L'\")", concreteElement)
            codeBlock.endControlFlow() // If
            codeBlock.endControlFlow() // For
        }

        if (createModelAtBeginning) {
            codeBlock.addStatement("return result")

        } else {
            val returnCodeBlock = CodeBlock.builder()
            returnCodeBlock.add("return new \$T(\n", concreteElement)
            returnCodeBlock.indent()

            for (i in flattenedFields.indices) {
                returnCodeBlock.add(flattenedFields[i].variableName)

                if (i < flattenedFields.size - 1) {
                    returnCodeBlock.add(",")
                }

                returnCodeBlock.add("\n")
            }

            returnCodeBlock.unindent()
            returnCodeBlock.add(");\n")
            codeBlock.add(returnCodeBlock.build())

        }
        readMethod.addCode(codeBlock.build())

        return readMethod.build()
    }

    /**
     * public void write(JsonWriter out, ImageSizes value) throws IOException {
     */
    @Throws(ProcessingException::class)
    private fun createWriteMethod(elementClassName: ClassName,
                                  rootElements: GsonObject,
                                  serializeNulls: Boolean): MethodSpec {

        val writeMethod = MethodSpec.methodBuilder("write")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(JsonWriter::class.java, "out")
                .addParameter(elementClassName, "value")
                .addException(IOException::class.java)

        val codeBlock = CodeBlock.builder()

        // Initial block which prevents nulls being accessed.
        codeBlock.beginControlFlow("if (value == null)")
        codeBlock.addStatement("out.nullValue()")
        codeBlock.addStatement("return")
        codeBlock.endControlFlow()

        codeBlock.add("\n")
        codeBlock.add("// Begin\n")

        mSafeWriteVariableCount = 0
        writeObject(0, codeBlock, rootElements, "", serializeNulls)

        writeMethod.addCode(codeBlock.build())
        return writeMethod.build()
    }

    @Throws(ProcessingException::class)
    private fun writeObject(fieldDepth: Int,
                            codeBlock: CodeBlock.Builder,
                            jsonMapping: GsonObject,
                            currentPath: String,
                            serializeNulls: Boolean) {

        codeBlock.addStatement("out.beginObject()")

        for (key in jsonMapping.keySet()) {
            val value = jsonMapping[key]

            if (value is GsonField) {
                val fieldInfo = value.fieldInfo

                // Make sure the field's annotations don't have any problems.
                validateFieldAnnotations(fieldInfo)

                val fieldTypeName = fieldInfo.typeName
                val isPrimitive = fieldTypeName.isPrimitive

                val objectName = "obj" + mSafeWriteVariableCount
                mSafeWriteVariableCount++

                codeBlock.addStatement("\$T \$L = value.\$L", fieldTypeName, objectName, fieldInfo.fieldName)

                // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
                if (!isPrimitive && !serializeNulls) {
                    codeBlock.beginControlFlow("if (\$L != null)", objectName)
                }
                codeBlock.addStatement("out.name(\"\$L\")", key)

                // Since we are serializing nulls, we defer the if-statement until after the name is written.
                if (!isPrimitive && serializeNulls) {
                    codeBlock.beginControlFlow("if (\$L != null)", objectName)
                }

                if (isPrimitive || GSON_SUPPORTED_CLASSES.contains(fieldTypeName)) {

                    codeBlock.addStatement("out.value(\$L)", objectName)

                } else {
                    val adapterName: String

                    if (fieldTypeName is ParameterizedTypeName) {
                        // This is a generic type
                        adapterName = String.format("new com.google.gson.reflect.TypeToken<%s>(){}", fieldTypeName)

                    } else {
                        adapterName = fieldTypeName.toString() + ".class"
                    }

                    codeBlock.addStatement("mGson.getAdapter(\$L).write(out, \$L)", adapterName, objectName)

                }

                // If we are serializing nulls, we need to ensure we output it here.
                if (!isPrimitive) {
                    if (serializeNulls) {
                        codeBlock.nextControlFlow("else")
                        codeBlock.addStatement("out.nullValue()")
                    }
                    codeBlock.endControlFlow()
                }
                codeBlock.add("\n")

            } else {
                val nextLevelMap = value as GsonObject
                if (nextLevelMap.size() > 0) {
                    val newPath: String
                    if (currentPath.isNotEmpty()) {
                        newPath = currentPath + "." + key
                    } else {
                        newPath = key
                    }

                    // Add a comment mentioning what nested object we are current pointing at.
                    codeBlock.add("\n// Begin \$L\n", newPath)
                    codeBlock.addStatement("out.name(\"\$L\")", key)
                    writeObject(fieldDepth + 1, codeBlock, nextLevelMap, newPath, serializeNulls)
                }
            }
        }

        codeBlock.add("// End \$L\n", currentPath)
        codeBlock.addStatement("out.endObject()")
    }

    public override fun onJavaFileBuilt(builder: JavaFile.Builder) {
        builder.addStaticImport(GsonUtil::class.java, "*")
    }

    @Throws(ProcessingException::class)
    private fun getDefaultsAnnotation(autoGsonAnnotation: AutoGsonAdapter): GsonPathDefaultConfiguration? {
        // Annotation processors seem to make obtaining this value difficult!
        var defaultsTypeMirror: TypeMirror? = null

        try {
            autoGsonAnnotation.defaultConfiguration
        } catch (mte: MirroredTypeException) {
            defaultsTypeMirror = mte.typeMirror
        }

        val defaultsElement = processingEnv.typeUtils.asElement(defaultsTypeMirror)

        var defaultsAnnotation: GsonPathDefaultConfiguration? = null
        if (defaultsElement != null) {
            // If an inheritable annotation is used, used the default instead.
            defaultsAnnotation = defaultsElement.getAnnotation(GsonPathDefaultConfiguration::class.java)

            if (defaultsAnnotation == null) {
                throw ProcessingException("Defaults property must point to a class which uses the @GsonPathDefaultConfiguration annotation")
            }
        }

        return defaultsAnnotation
    }

    @Throws(ProcessingException::class)
    private fun addGsonAdapterReadCode(codeBlock: CodeBlock.Builder,
                                       jsonMapping: GsonObject,
                                       createModelAtBeginning: Boolean,
                                       mandatoryInfoMap: Map<String, MandatoryFieldInfo>,
                                       concreteElement: ClassName,
                                       flattenedFields: List<GsonField>) {

        mCounterVariableCount = 0
        addGsonAdapterReadCodeInternal(codeBlock, jsonMapping, createModelAtBeginning,
                mandatoryInfoMap, concreteElement, flattenedFields, 0)
    }

    @Throws(ProcessingException::class)
    private fun addGsonAdapterReadCodeInternal(codeBlock: CodeBlock.Builder,
                                               jsonMapping: GsonObject,
                                               createModelAtBeginning: Boolean,
                                               mandatoryInfoMap: Map<String, MandatoryFieldInfo>,
                                               concreteElement: ClassName,
                                               flattenedFields: List<GsonField>,
                                               fieldDepth: Int) {

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
            codeBlock.addStatement("return null")

        } else {
            codeBlock.addStatement("break")
        }

        codeBlock.endControlFlow() // if

        // This is the first block of code to fire after the object is valid.
        if (fieldDepth == 0) {
            if (createModelAtBeginning) {
                codeBlock.addStatement("\$T result = new \$T()", concreteElement, concreteElement)

            } else {
                for (gsonField in flattenedFields) {
                    // Don't initialise primitives, we rely on validation to throw an exception if the value does not exist.
                    val typeName = gsonField.fieldInfo.typeName
                    val defaultValue = adapterGeneratorUtils.createDefaultVariableValueForTypeName(typeName)

                    codeBlock.addStatement("%s %s = %s".format(typeName, gsonField.variableName, defaultValue),
                            typeName,
                            gsonField.variableName)
                }
            }

            // If we have any mandatory fields, we need to keep track of what has been assigned.
            if (mandatoryInfoMap.isNotEmpty()) {
                codeBlock.addStatement("boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE]")
            }

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
                    handleGsonField(value, codeBlock, createModelAtBeginning, mandatoryInfoMap)
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
                handleGsonField(value, codeBlock, createModelAtBeginning, mandatoryInfoMap)

            } else {
                val nextLevelMap = value as GsonObject
                if (nextLevelMap.size() == 0) {
                    addBreak = false

                } else {
                    addGsonAdapterReadCodeInternal(codeBlock, nextLevelMap, createModelAtBeginning,
                            mandatoryInfoMap,
                            concreteElement,
                            flattenedFields,
                            fieldDepth + 1)
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
                                mandatoryInfoMap: Map<String, MandatoryFieldInfo>) {

        val fieldInfo = gsonField.fieldInfo

        // Make sure the field's annotations don't have any problems.
        validateFieldAnnotations(fieldInfo)

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


            val mandatoryFieldInfo = mandatoryInfoMap[fieldName]

            // When a field has been assigned, if it is a mandatory value, we note this down.
            if (mandatoryFieldInfo != null) {
                codeBlock.addStatement("mandatoryFieldsCheckList[\$L] = true", mandatoryFieldInfo.indexVariableName)
                codeBlock.add("\n")
            }

            if (gsonField.isRequired) {
                codeBlock.nextControlFlow("else")
                codeBlock.addStatement("throw new gsonpath.JsonFieldMissingException(\"Mandatory " + "JSON element '\$L' was null for class '\$L'\")",
                        gsonField.jsonPath,
                        fieldInfo.parentClassName)
            }

            codeBlock.endControlFlow() // if
        }
    }

    private fun validateFieldAnnotations(fieldInfo: FieldInfo) {
        // For now, we only ensure that the flatten annotation is only added to a String.
        if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
            return
        }

        if (fieldInfo.typeName != CLASS_NAME_STRING) {
            throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
        }
    }
}
