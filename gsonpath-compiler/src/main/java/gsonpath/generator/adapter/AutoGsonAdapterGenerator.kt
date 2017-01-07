package gsonpath.generator.adapter

import com.google.gson.Gson
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

class AutoGsonAdapterGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {
    private val adapterGeneratorDelegate: AdapterGeneratorDelegate = AdapterGeneratorDelegate()
    private val annotationValidator: AnnotationValidator = AnnotationValidator()
    private var mSafeWriteVariableCount: Int = 0

    @Throws(ProcessingException::class)
    fun handle(modelElement: TypeElement): HandleResult {
        val modelClassName = ClassName.get(modelElement)
        val adapterClassName = ClassName.get(modelClassName.packageName(),
                adapterGeneratorDelegate.generateClassName(modelClassName, "GsonTypeAdapter"))

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

        val objectParserCallback = AdapterObjectParserCallback(
                codeBlock, mandatoryInfoMap, concreteElement, flattenedFields, createModelAtBeginning)

        adapterGeneratorDelegate.addGsonAdapterReadCode(codeBlock, rootElements, createModelAtBeginning,
                annotationValidator, objectParserCallback)

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
                annotationValidator.validateFieldAnnotations(fieldInfo)

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

                if (isPrimitive || AdapterGeneratorDelegate.GSON_SUPPORTED_CLASSES.contains(fieldTypeName)) {

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

    private class AdapterObjectParserCallback constructor(private val codeBlock: CodeBlock.Builder,
                                                          private val mandatoryInfoMap: Map<String, MandatoryFieldInfo>,
                                                          private val concreteElement: ClassName,
                                                          private val flattenedFields: List<GsonField>,
                                                          private val createModelAtBeginning: Boolean) : AdapterGeneratorDelegate.ObjectParserCallback {

        override fun onInitialObjectNull() {
            codeBlock.addStatement("return null")
        }

        override fun onInitialise() {
            if (createModelAtBeginning) {
                codeBlock.addStatement("\$T result = new \$T()", concreteElement, concreteElement)

            } else {
                for (gsonField in flattenedFields) {
                    // Don't initialise primitives, we rely on validation to throw an exception if the value does not exist.
                    val typeName = gsonField.fieldInfo.typeName

                    val defaultValue: String
                    when (typeName) {
                        TypeName.INT,
                        TypeName.BYTE,
                        TypeName.SHORT ->
                            defaultValue = "0"

                        TypeName.LONG ->
                            defaultValue = "0L"

                        TypeName.FLOAT ->
                            defaultValue = "0f"

                        TypeName.DOUBLE ->
                            defaultValue = "0d"

                        TypeName.CHAR ->
                            defaultValue = "'\\u0000'"

                        TypeName.BOOLEAN ->
                            defaultValue = "false"

                        else ->
                            defaultValue = "null"
                    }

                    codeBlock.addStatement("%s %s = %s".format(typeName, gsonField.variableName, defaultValue),
                            typeName,
                            gsonField.variableName)
                }
            }

            // If we have any mandatory fields, we need to keep track of what has been assigned.
            if (mandatoryInfoMap.isNotEmpty()) {
                codeBlock.addStatement("boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE]")
            }
        }

        override fun onFieldAssigned(fieldName: String) {
            val mandatoryFieldInfo = mandatoryInfoMap[fieldName]

            // When a field has been assigned, if it is a mandatory value, we note this down.
            if (mandatoryFieldInfo != null) {
                codeBlock.addStatement("mandatoryFieldsCheckList[\$L] = true", mandatoryFieldInfo.indexVariableName)
                codeBlock.add("\n")
            }
        }

        override fun onNodeEmpty() {
        }
    }

    private class AnnotationValidator : AdapterGeneratorDelegate.FieldAnnotationValidator {
        val CLASS_NAME_STRING: ClassName = ClassName.get(String::class.java)

        @Throws(ProcessingException::class)
        override fun validateFieldAnnotations(fieldInfo: FieldInfo) {
            // For now, we only ensure that the flatten annotation is only added to a String.
            if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
                return
            }

            if (fieldInfo.typeName != CLASS_NAME_STRING) {
                throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
            }
        }
    }
}
