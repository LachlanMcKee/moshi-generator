package gsonpath.generator.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.squareup.javapoet.*;
import gsonpath.*;
import gsonpath.generator.*;
import gsonpath.model.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.*;

public class AutoGsonAdapterGenerator extends Generator {
    private final AdapterGeneratorDelegate adapterGeneratorDelegate;
    private final AnnotationValidator annotationValidator;
    private int mSafeWriteVariableCount;

    public AutoGsonAdapterGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        adapterGeneratorDelegate = new AdapterGeneratorDelegate();
        annotationValidator = new AnnotationValidator();
    }

    public HandleResult handle(TypeElement modelElement) throws ProcessingException {
        ClassName modelClassName = ClassName.get(modelElement);
        ClassName adapterClassName = ClassName.get(modelClassName.packageName(),
                adapterGeneratorDelegate.generateClassName(modelClassName, "GsonTypeAdapter"));

        TypeSpec.Builder adapterTypeBuilder = TypeSpec.classBuilder(adapterClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter.class), modelClassName))
                .addField(Gson.class, "mGson", Modifier.PRIVATE, Modifier.FINAL);

        // Add the constructor which takes a gson instance for future use.
        adapterTypeBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Gson.class, "gson")
                .addStatement("this.$N = $N", "mGson", "gson")
                .build());

        AutoGsonAdapter autoGsonAnnotation = modelElement.getAnnotation(AutoGsonAdapter.class);
        GsonPathDefaultConfiguration defaultsAnnotation = getDefaultsAnnotation(autoGsonAnnotation);

        final ClassName concreteClassName;
        final List<FieldInfo> fieldInfoList;
        final boolean isModelInterface = modelElement.getKind().isInterface();

        AutoGsonAdapterProperties properties = new AutoGsonAdapterPropertiesFactory().create(
                autoGsonAnnotation, defaultsAnnotation, isModelInterface);

        FieldInfoFactory fieldInfoFactory = new FieldInfoFactory(processingEnv);
        if (!isModelInterface) {
            concreteClassName = modelClassName;

            fieldInfoList = fieldInfoFactory.getModelFieldsFromElement(modelElement, properties.fieldsRequireAnnotation);

        } else {
            InterfaceInfo interfaceInfo = new ModelInterfaceGenerator(processingEnv).handle(modelElement);
            concreteClassName = interfaceInfo.parentClassName;

            fieldInfoList = fieldInfoFactory.getModelFieldsFromInterface(interfaceInfo);
        }

        GsonObject rootGsonObject = new GsonObjectTreeFactory().createGsonObject(fieldInfoList, properties.rootField,
                properties.flattenDelimiter, properties.gsonFieldNamingPolicy, properties.gsonFieldValidationType,
                properties.pathSubstitutions);

        // Adds the mandatory field index constants and also populates the mandatoryInfoMap values.
        Map<String, MandatoryFieldInfo> mandatoryInfoMap = new MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootGsonObject);

        int mandatoryFieldSize = mandatoryInfoMap.size();
        if (mandatoryFieldSize > 0) {

            int mandatoryIndex = 0;
            for (MandatoryFieldInfo mandatoryField : mandatoryInfoMap.values()) {
                adapterTypeBuilder.addField(FieldSpec.builder(TypeName.INT, mandatoryField.indexVariableName)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("" + (mandatoryIndex++))
                        .build());
            }

            adapterTypeBuilder.addField(FieldSpec.builder(TypeName.INT, "MANDATORY_FIELDS_SIZE")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("" + mandatoryFieldSize)
                    .build());
        }

        adapterTypeBuilder.addMethod(createReadMethod(modelClassName, concreteClassName, mandatoryInfoMap, rootGsonObject));

        if (!isModelInterface) {
            adapterTypeBuilder.addMethod(createWriteMethod(modelClassName, rootGsonObject, properties.serializeNulls));

        } else {
            // Create an empty method for the write, since we do not support writing for interfaces.
            MethodSpec.Builder writeMethod = MethodSpec.methodBuilder("write")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(JsonWriter.class, "out")
                    .addParameter(modelClassName, "value")
                    .addException(IOException.class);

            adapterTypeBuilder.addMethod(writeMethod.build());
        }

        if (writeFile(adapterClassName.packageName(), adapterTypeBuilder)) {
            return new HandleResult(modelClassName, adapterClassName);
        }

        throw new ProcessingException("Failed to write generated file: " + adapterClassName.simpleName());
    }

    /**
     * public ImageSizes read(JsonReader in) throws IOException {
     */
    private MethodSpec createReadMethod(ClassName baseElement,
                                        ClassName concreteElement,
                                        Map<String, MandatoryFieldInfo> mandatoryInfoMap,
                                        GsonObject rootElements) throws ProcessingException {

        // Create a flat list of the variables and ensure they are ordered by their original field index within the POJO
        List<GsonField> flattenedFields = new GsonObjectTreeFactory().getFlattenedFieldsFromGsonObject(rootElements);

        MethodSpec.Builder readMethod = MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(baseElement)
                .addParameter(JsonReader.class, "in")
                .addException(IOException.class);

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        boolean createModelAtBeginning = baseElement.equals(concreteElement);

        AdapterObjectParserCallback objectParserCallback = new AdapterObjectParserCallback(
                codeBlock, mandatoryInfoMap, concreteElement, flattenedFields, createModelAtBeginning);

        adapterGeneratorDelegate.addGsonAdapterReadCode(codeBlock, rootElements, createModelAtBeginning,
                annotationValidator, objectParserCallback);

        // If we have any mandatory fields, we now check if any values have been missed. If they are, it will raise an exception here.
        if (mandatoryInfoMap.size() > 0) {
            codeBlock.add("\n// Mandatory object validation\n");
            codeBlock.beginControlFlow("for (int mandatoryFieldIndex = 0; " +
                    "mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++)");

            codeBlock.add("\n// Check if a mandatory value is missing.\n");
            codeBlock.beginControlFlow("if (!mandatoryFieldsCheckList[mandatoryFieldIndex])");

            // The code must figure out the correct field name to insert into the error message.
            codeBlock.add("\n// Find the field name of the missing json value.\n");
            codeBlock.addStatement("String fieldName = null");
            codeBlock.beginControlFlow("switch (mandatoryFieldIndex)");

            for (String mandatoryKey : mandatoryInfoMap.keySet()) {
                MandatoryFieldInfo mandatoryFieldInfo = mandatoryInfoMap.get(mandatoryKey);
                codeBlock.add("case $L:\n", mandatoryFieldInfo.indexVariableName);
                codeBlock.indent();
                codeBlock.addStatement("fieldName = \"$L\"", mandatoryFieldInfo.gsonField.jsonPath);
                codeBlock.addStatement("break");
                codeBlock.unindent();
                codeBlock.add("\n");
            }

            codeBlock.endControlFlow(); // Switch
            codeBlock.addStatement("throw new gsonpath.JsonFieldMissingException(\"Mandatory JSON " +
                    "element '\" + fieldName + \"' was not found for class '$L'\")", concreteElement);
            codeBlock.endControlFlow(); // If
            codeBlock.endControlFlow(); // For
        }

        if (createModelAtBeginning) {
            codeBlock.addStatement("return result");

        } else {
            CodeBlock.Builder returnCodeBlock = CodeBlock.builder();
            returnCodeBlock.add("return new $T(\n", concreteElement);
            returnCodeBlock.indent();

            for (int i = 0; i < flattenedFields.size(); i++) {
                returnCodeBlock.add(flattenedFields.get(i).getVariableName());

                if (i < flattenedFields.size() - 1) {
                    returnCodeBlock.add(",");
                }

                returnCodeBlock.add("\n");
            }

            returnCodeBlock.unindent();
            returnCodeBlock.add(");\n");
            codeBlock.add(returnCodeBlock.build());

        }
        readMethod.addCode(codeBlock.build());

        return readMethod.build();
    }

    /**
     * public void write(JsonWriter out, ImageSizes value) throws IOException {
     */
    private MethodSpec createWriteMethod(ClassName elementClassName,
                                         GsonObject rootElements,
                                         boolean serializeNulls) throws ProcessingException {

        MethodSpec.Builder writeMethod = MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(JsonWriter.class, "out")
                .addParameter(elementClassName, "value")
                .addException(IOException.class);

        CodeBlock.Builder codeBlock = CodeBlock.builder();

        // Initial block which prevents nulls being accessed.
        codeBlock.beginControlFlow("if (value == null)");
        codeBlock.addStatement("out.nullValue()");
        codeBlock.addStatement("return");
        codeBlock.endControlFlow();

        codeBlock.add("\n");
        codeBlock.add("// Begin\n");

        mSafeWriteVariableCount = 0;
        writeObject(0, codeBlock, rootElements, "", serializeNulls);

        writeMethod.addCode(codeBlock.build());
        return writeMethod.build();
    }

    private void writeObject(int fieldDepth,
                             CodeBlock.Builder codeBlock,
                             GsonObject jsonMapping,
                             String currentPath,
                             boolean serializeNulls) throws ProcessingException {

        codeBlock.addStatement("out.beginObject()");

        for (String key : jsonMapping.keySet()) {
            Object value = jsonMapping.get(key);

            if (value instanceof GsonField) {
                GsonField gsonField = (GsonField) value;
                FieldInfo fieldInfo = gsonField.fieldInfo;

                // Make sure the field's annotations don't have any problems.
                annotationValidator.validateFieldAnnotations(fieldInfo);

                TypeName fieldTypeName = fieldInfo.getTypeName();
                boolean isPrimitive = fieldTypeName.isPrimitive();

                String objectName = "obj" + mSafeWriteVariableCount;
                mSafeWriteVariableCount++;

                codeBlock.addStatement("$T $L = value.$L", fieldTypeName, objectName, fieldInfo.getFieldName());

                // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
                if (!isPrimitive && !serializeNulls) {
                    codeBlock.beginControlFlow("if ($L != null)", objectName);
                }
                codeBlock.addStatement("out.name(\"$L\")", key);

                // Since we are serializing nulls, we defer the if-statement until after the name is written.
                if (!isPrimitive && serializeNulls) {
                    codeBlock.beginControlFlow("if ($L != null)", objectName);
                }

                if (isPrimitive || AdapterGeneratorDelegate.GSON_SUPPORTED_CLASSES.contains(fieldTypeName)) {

                    codeBlock.addStatement("out.value($L)", objectName);

                } else {
                    String adapterName;

                    if (fieldTypeName instanceof ParameterizedTypeName) {
                        // This is a generic type
                        adapterName = String.format("new com.google.gson.reflect.TypeToken<%s>(){}", fieldTypeName);

                    } else {
                        adapterName = fieldTypeName + ".class";
                    }

                    codeBlock.addStatement("mGson.getAdapter($L).write(out, $L)", adapterName, objectName);

                }

                // If we are serializing nulls, we need to ensure we output it here.
                if (!isPrimitive) {
                    if (serializeNulls) {
                        codeBlock.nextControlFlow("else");
                        codeBlock.addStatement("out.nullValue()");
                    }
                    codeBlock.endControlFlow();
                }
                codeBlock.add("\n");

            } else {
                GsonObject nextLevelMap = (GsonObject) value;
                if (nextLevelMap.size() > 0) {
                    String newPath;
                    if (currentPath.length() > 0) {
                        newPath = currentPath + "." + key;
                    } else {
                        newPath = key;
                    }

                    // Add a comment mentioning what nested object we are current pointing at.
                    codeBlock.add("\n// Begin $L\n", newPath);
                    codeBlock.addStatement("out.name(\"$L\")", key);
                    writeObject(fieldDepth + 1, codeBlock, nextLevelMap, newPath, serializeNulls);
                }
            }
        }

        codeBlock.add("// End $L\n", currentPath);
        codeBlock.addStatement("out.endObject()");
    }

    @Override
    public void onJavaFileBuilt(JavaFile.Builder builder) {
        builder.addStaticImport(GsonUtil.class, "*");
    }

    private GsonPathDefaultConfiguration getDefaultsAnnotation(AutoGsonAdapter autoGsonAnnotation) throws ProcessingException {
        // Annotation processors seem to make obtaining this value difficult!
        TypeMirror defaultsTypeMirror = null;

        try {
            autoGsonAnnotation.defaultConfiguration();
        } catch (MirroredTypeException mte) {
            defaultsTypeMirror = mte.getTypeMirror();
        }

        Element defaultsElement = processingEnv.getTypeUtils().asElement(defaultsTypeMirror);

        GsonPathDefaultConfiguration defaultsAnnotation = null;
        if (defaultsElement != null) {
            // If an inheritable annotation is used, used the default instead.
            defaultsAnnotation = defaultsElement.getAnnotation(GsonPathDefaultConfiguration.class);

            if (defaultsAnnotation == null) {
                throw new ProcessingException("Defaults property must point to a class which uses the @GsonPathDefaultConfiguration annotation");
            }
        }

        return defaultsAnnotation;
    }

    private static class AdapterObjectParserCallback implements AdapterGeneratorDelegate.ObjectParserCallback {
        private final CodeBlock.Builder codeBlock;
        private final Map<String, MandatoryFieldInfo> mandatoryInfoMap;
        private final ClassName concreteElement;
        private final List<GsonField> flattenedFields;
        private final boolean createModelAtBeginning;

        private AdapterObjectParserCallback(CodeBlock.Builder codeBlock, Map<String, MandatoryFieldInfo> mandatoryInfoMap,
                                            ClassName concreteElement, List<GsonField> flattenedFields, boolean createModelAtBeginning) {
            this.codeBlock = codeBlock;
            this.mandatoryInfoMap = mandatoryInfoMap;
            this.concreteElement = concreteElement;
            this.flattenedFields = flattenedFields;
            this.createModelAtBeginning = createModelAtBeginning;
        }

        @Override
        public void onInitialObjectNull() {
            codeBlock.addStatement("return null");
        }

        @Override
        public void onInitialise() {
            if (createModelAtBeginning) {
                codeBlock.addStatement("$T result = new $T()", concreteElement, concreteElement);

            } else {
                for (GsonField gsonField : flattenedFields) {
                    FieldInfo fieldInfo = gsonField.fieldInfo;

                    // Don't initialise primitives, we rely on validation to throw an exception if the value does not exist.
                    TypeName typeName = fieldInfo.getTypeName();
                    String variableDeclaration = String.format("%s %s", typeName, gsonField.getVariableName());

                    String defaultValue = "null";
                    if (typeName.isPrimitive()) {
                        if (typeName == TypeName.INT || typeName == TypeName.BYTE || typeName == TypeName.SHORT) {
                            defaultValue = "0";

                        } else if (typeName == TypeName.LONG) {
                            defaultValue = "0L";

                        } else if (typeName == TypeName.FLOAT) {
                            defaultValue = "0f";

                        } else if (typeName == TypeName.DOUBLE) {
                            defaultValue = "0d";

                        } else if (typeName == TypeName.CHAR) {
                            defaultValue = "'\\0'";

                        } else if (typeName == TypeName.BOOLEAN) {
                            defaultValue = "false";
                        }
                    }

                    codeBlock.addStatement(variableDeclaration + " = " + defaultValue,
                            typeName,
                            gsonField.getVariableName());
                }
            }

            // If we have any mandatory fields, we need to keep track of what has been assigned.
            if (mandatoryInfoMap.size() > 0) {
                codeBlock.addStatement("boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE]");
            }
        }

        @Override
        public void onFieldAssigned(String fieldName) {
            MandatoryFieldInfo mandatoryFieldInfo = mandatoryInfoMap.get(fieldName);

            // When a field has been assigned, if it is a mandatory value, we note this down.
            if (mandatoryFieldInfo != null) {
                codeBlock.addStatement("mandatoryFieldsCheckList[$L] = true", mandatoryFieldInfo.indexVariableName);
                codeBlock.add("\n");
            }
        }

        @Override
        public void onNodeEmpty() {
        }
    }

    private static class AnnotationValidator implements AdapterGeneratorDelegate.FieldAnnotationValidator {
        private static final ClassName CLASS_NAME_STRING = ClassName.get(String.class);

        @Override
        public void validateFieldAnnotations(FieldInfo fieldInfo) throws ProcessingException {
            // For now, we only ensure that the flatten annotation is only added to a String.
            if (fieldInfo.getAnnotation(FlattenJson.class) == null) {
                return;
            }

            if (!fieldInfo.getTypeName().equals(CLASS_NAME_STRING)) {
                throw new ProcessingException("FlattenObject can only be used on String variables", fieldInfo.getElement());
            }
        }
    }
}
