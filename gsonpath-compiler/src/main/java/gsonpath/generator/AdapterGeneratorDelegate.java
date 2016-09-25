package gsonpath.generator;

import com.google.gson.JsonElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import gsonpath.FlattenJson;
import gsonpath.ProcessingException;
import gsonpath.model.FieldInfo;
import gsonpath.model.GsonField;
import gsonpath.model.GsonTree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AdapterGeneratorDelegate {
    private static final Set<TypeName> GSON_SUPPORTED_PRIMITIVE = new HashSet<>(Arrays.asList(
            TypeName.BOOLEAN,
            TypeName.INT,
            TypeName.LONG,
            TypeName.DOUBLE
    ));

    public static final Set<TypeName> GSON_SUPPORTED_CLASSES = new HashSet<>(Arrays.asList(
            TypeName.get(Boolean.class),
            TypeName.get(Integer.class),
            TypeName.get(Long.class),
            TypeName.get(Double.class),
            TypeName.get(String.class)
    ));

    private static final ClassName CLASS_NAME_STRING = ClassName.get(String.class);
    private static final ClassName CLASS_NAME_JSON_ELEMENT = ClassName.get(JsonElement.class);

    // Used to avoid naming conflicts.
    private int mCounterVariableCount;

    public void addGsonAdapterReadCode(CodeBlock.Builder codeBlock,
                                       GsonTree jsonMapping,
                                       boolean createModelAtBeginning,
                                       FieldAnnotationValidator fieldAnnotationValidator,
                                       ObjectParserCallback callback) throws ProcessingException {

        mCounterVariableCount = 0;
        addGsonAdapterReadCodeInternal(codeBlock, jsonMapping, createModelAtBeginning, 0, fieldAnnotationValidator, callback);
    }

    private void addGsonAdapterReadCodeInternal(CodeBlock.Builder codeBlock,
                                                GsonTree jsonMapping,
                                                boolean createModelAtBeginning,
                                                int fieldDepth,
                                                FieldAnnotationValidator fieldAnnotationValidator,
                                                ObjectParserCallback callback) throws ProcessingException {

        String counterVariableName = "jsonFieldCounter" + mCounterVariableCount;
        mCounterVariableCount++;

        //
        // Ensure a Json object exists begin attempting to read it.
        //
        codeBlock.add("\n");
        codeBlock.add("// Ensure the object is not null.\n");
        codeBlock.beginControlFlow("if (!isValidValue(in))");

        if (fieldDepth == 0) {
            // Allow the calling method to inject different logic. Typically this would be to return.
            callback.onInitialObjectNull();

        } else {
            codeBlock.addStatement("break");
        }

        codeBlock.endControlFlow(); // if

        // This is the first block of code to fire after the object is valid.
        if (fieldDepth == 0) {
            callback.onInitialise();
            codeBlock.add("\n");
        }

        if (jsonMapping.size() == 0) {
            return;
        }

        codeBlock.addStatement("int $L = 0", counterVariableName);
        codeBlock.addStatement("in.beginObject()");
        codeBlock.add("\n");
        codeBlock.beginControlFlow("while (in.hasNext())");

        //
        // Since all the required fields have been mapped, we can avoid calling 'nextName'.
        // This ends up yielding performance improvements on large datasets depending on
        // the ordering of the fields within the JSON.
        //
        codeBlock.beginControlFlow("if ($L == $L)", counterVariableName, jsonMapping.size());
        codeBlock.addStatement("in.skipValue()");
        codeBlock.addStatement("continue");
        codeBlock.endControlFlow(); // if
        codeBlock.add("\n");

        codeBlock.beginControlFlow("switch (in.nextName())");

        boolean addBreak = true;
        for (String key : jsonMapping.keySet()) {
            codeBlock.add("case \"$L\":\n", key);
            codeBlock.indent();

            // Increment the counter to ensure we track how many fields we have mapped.
            codeBlock.addStatement("$L++", counterVariableName);

            Object value = jsonMapping.get(key);
            if (value instanceof GsonField) {
                handleGsonField((GsonField) value, codeBlock, createModelAtBeginning, fieldAnnotationValidator, callback);

            } else {
                GsonTree nextLevelMap = (GsonTree) value;
                if (nextLevelMap.size() == 0) {
                    callback.onNodeEmpty();
                    addBreak = false;
                } else {
                    addGsonAdapterReadCodeInternal(codeBlock, nextLevelMap, createModelAtBeginning,
                            fieldDepth + 1,
                            fieldAnnotationValidator,
                            callback);
                }
            }

            if (addBreak) {
                codeBlock.addStatement("break");
            }

            codeBlock.add("\n");
            codeBlock.unindent();
        }

        codeBlock.add("default:\n");
        codeBlock.indent();
        codeBlock.addStatement("in.skipValue()");
        codeBlock.addStatement("break");
        codeBlock.unindent();

        codeBlock.endControlFlow(); // switch
        codeBlock.endControlFlow(); // while
        codeBlock.add("\n");

        codeBlock.add("\n");

        codeBlock.addStatement("in.endObject()");
    }

    private void handleGsonField(GsonField gsonField, CodeBlock.Builder codeBlock, boolean createModelAtBeginning,
                                 FieldAnnotationValidator fieldAnnotationValidator,
                                 ObjectParserCallback callback) throws ProcessingException {
        FieldInfo fieldInfo = gsonField.fieldInfo;

        // Make sure the field's annotations don't have any problems.
        if (fieldAnnotationValidator != null) {
            fieldAnnotationValidator.validateFieldAnnotations(fieldInfo);
        }

        TypeName fieldTypeName = fieldInfo.getTypeName();

        // Add a new line to improve readability for the multi-lined mapping.
        codeBlock.add("\n");

        String variableName = gsonField.getVariableName();
        String safeVariableName = variableName;

        // A model isn't created if the constructor is called at the bottom of the type adapter.
        boolean checkIfResultIsNull = createModelAtBeginning;
        if (gsonField.isRequired && !createModelAtBeginning) {
            safeVariableName += "_safe";
            checkIfResultIsNull = true;
        }

        boolean callToString = false;

        // If the field type is primitive, ensure that it is a supported primitive.
        if (fieldTypeName.isPrimitive() && !GSON_SUPPORTED_PRIMITIVE.contains(fieldTypeName)) {
            throw new ProcessingException("Unsupported primitive type found. Only boolean, int, double and long can be used.", fieldInfo.getElement());
        }

        if (GSON_SUPPORTED_CLASSES.contains(fieldTypeName.box())) {
            ClassName fieldClassName = (ClassName) fieldTypeName.box();

            // Special handling for strings.
            boolean handled = false;
            if (fieldTypeName.equals(CLASS_NAME_STRING)) {
                FlattenJson annotation = fieldInfo.getAnnotation(FlattenJson.class);
                if (annotation != null) {
                    handled = true;

                    // FlattenJson is a special case. We always need to ensure that the JsonObject is not null.
                    if (!checkIfResultIsNull) {
                        safeVariableName += "_safe";
                        checkIfResultIsNull = true;
                    }

                    codeBlock.addStatement("$T $L = mGson.getAdapter($T.class).read(in)",
                            CLASS_NAME_JSON_ELEMENT,
                            safeVariableName,
                            CLASS_NAME_JSON_ELEMENT);

                    callToString = true;
                }
            }

            if (!handled) {
                String variableAssignment = String.format("%s = get%sSafely(in)",
                        safeVariableName,
                        fieldClassName.simpleName());

                if (checkIfResultIsNull) {
                    codeBlock.addStatement("$L $L", fieldClassName.simpleName(), variableAssignment);

                } else {
                    codeBlock.addStatement(variableAssignment);
                }
            }
        } else {
            String adapterName;

            if (fieldTypeName instanceof ParameterizedTypeName) {
                // This is a generic type
                adapterName = String.format("new com.google.gson.reflect.TypeToken<%s>(){}", fieldTypeName);

            } else {
                adapterName = fieldTypeName + ".class";
            }

            // Handle every other possible class by falling back onto the gson adapter.
            String variableAssignment = String.format("%s = mGson.getAdapter(%s).read(in)",
                    safeVariableName,
                    adapterName);

            if (checkIfResultIsNull) {
                codeBlock.addStatement("$L $L", fieldTypeName, variableAssignment);

            } else {
                codeBlock.addStatement(variableAssignment);
            }
        }

        if (checkIfResultIsNull) {
            String fieldName = fieldInfo.getFieldName();
            codeBlock.beginControlFlow("if ($L != null)", safeVariableName);

            String assignmentBlock;
            if (createModelAtBeginning) {
                assignmentBlock = "result." + fieldName;
            } else {
                assignmentBlock = variableName;
            }

            codeBlock.addStatement("$L = $L$L",
                    assignmentBlock,
                    safeVariableName,
                    callToString ? ".toString()" : "");

            // Inform the callback in case it wishes to add any further code.
            callback.onFieldAssigned(fieldName);

            if (gsonField.isRequired) {
                codeBlock.nextControlFlow("else");
                codeBlock.addStatement("throw new gsonpath.JsonFieldMissingException(\"Mandatory " +
                                "JSON element '$L' was null for class '$L'\")",
                        gsonField.jsonPath,
                        fieldInfo.getParentClassName());
            }

            codeBlock.endControlFlow(); // if
        }
    }

    public String generateClassName(ClassName className, String classNameSuffix) {
        //
        // We need to ensure that nested classes are have include their parent class as part of the name.
        // Otherwise this could cause file name contention when other nested classes have the same name
        //
        String fileName = "";
        for (String name : className.simpleNames()) {
            fileName += name + "_";
        }

        // Make sure no '.' managed to sneak through!
        return fileName.replace(".", "_") + classNameSuffix;
    }

    public interface ObjectParserCallback {
        void onInitialObjectNull();

        void onInitialise();

        void onFieldAssigned(String fieldName);

        void onNodeEmpty();
    }

    public interface FieldAnnotationValidator {
        void validateFieldAnnotations(FieldInfo fieldInfo) throws ProcessingException;
    }
}
