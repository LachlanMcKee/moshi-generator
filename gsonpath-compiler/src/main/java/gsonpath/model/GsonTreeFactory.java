package gsonpath.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.TypeName;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;
import gsonpath.ProcessingException;

import javax.lang.model.element.Element;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class GsonTreeFactory {
    public GsonTree createGsonTree(List<FieldInfo> fieldInfoList,
                                   String rootField,
                                   char flattenDelimiter,
                                   FieldNamingPolicy gsonFieldNamingPolicy,
                                   GsonFieldValidationType gsonFieldValidationType,
                                   PathSubstitution[] pathSubstitutions) throws ProcessingException {

        // Obtain the correct mapping structure beforehand.
        GsonTree absoluteRootFieldTree = new GsonTree();
        GsonTree gsonPathFieldTree = absoluteRootFieldTree;

        if (rootField.length() > 0) {
            gsonPathFieldTree = createGsonTreeFromRootField(gsonPathFieldTree, rootField, flattenDelimiter);

        } else {
            gsonPathFieldTree = absoluteRootFieldTree;
        }

        for (int fieldInfoIndex = 0; fieldInfoIndex < fieldInfoList.size(); fieldInfoIndex++) {
            FieldInfo fieldInfo = fieldInfoList.get(fieldInfoIndex);
            TypeName fieldTypeName = fieldInfo.getTypeName();

            if (fieldTypeName.equals(TypeName.OBJECT)) {
                throw new ProcessingException("Invalid field type: " + fieldTypeName, fieldInfo.getElement());
            }

            SerializedName serializedNameAnnotation = fieldInfo.getAnnotation(SerializedName.class);
            String fieldName = fieldInfo.getFieldName();
            String jsonFieldPath;

            if (serializedNameAnnotation != null && serializedNameAnnotation.value().length() > 0) {
                jsonFieldPath = serializedNameAnnotation.value();

                // Check if the serialized name needs any values to be substituted
                for (PathSubstitution substitution : pathSubstitutions) {
                    jsonFieldPath = jsonFieldPath.replaceAll("\\{" + substitution.original() + "\\}", substitution.replacement());
                }

            } else {
                // Since the serialized annotation wasn't specified, we need to apply the naming policy instead.
                jsonFieldPath = applyFieldNamingPolicy(gsonFieldNamingPolicy, fieldName);
            }

            boolean isMandatory = false;
            boolean isOptional = false;

            // Attempt to find a Nullable or NonNull annotation type.
            for (String annotationName : fieldInfo.getAnnotationNames()) {
                switch (annotationName) {
                    case "Nullable":
                        isOptional = true;
                        break;

                    // Intentional fall-through. There are several different variations!
                    case "NonNull":
                    case "Nonnull":
                    case "NotNull":
                    case "Notnull":
                        isMandatory = true;
                        break;
                }
            }

            // Fields cannot use both annotations.
            if (isMandatory && isOptional) {
                throw new ProcessingException("Field cannot have both Mandatory and Optional annotations", fieldInfo.getElement());
            }

            // Primitives should not use either annotation.
            boolean isPrimitive = fieldTypeName.isPrimitive();
            if (isPrimitive && (isMandatory || isOptional)) {
                throw new ProcessingException("Primitives should not use NonNull or Nullable annotations", fieldInfo.getElement());
            }

            boolean isRequired = isMandatory;

            switch (gsonFieldValidationType) {
                case VALIDATE_ALL_EXCEPT_NULLABLE:
                    // Using this policy everything is mandatory except for optionals.
                    isRequired = true;
                    break;

                case VALIDATE_EXPLICIT_NON_NULL:
                    // Primitives are treated as non-null implicitly.
                    if (isPrimitive) {
                        isRequired = true;
                    }
                    break;
            }

            // Optionals will never fail regardless of the policy.
            if (isOptional || gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION) {
                isRequired = false;
            }

            if (jsonFieldPath.contains(String.valueOf(flattenDelimiter))) {
                //
                // When the last character is a delimiter, we should append the variable name to
                // the end of the field name, as this may reduce annotation repetition.
                //
                if (jsonFieldPath.charAt(jsonFieldPath.length() - 1) == flattenDelimiter) {
                    jsonFieldPath += fieldName;
                }

                // Ensure that the delimiter is correctly escaped before attempting to split the string.
                String regexSafeDelimiter = Pattern.quote(String.valueOf(flattenDelimiter));
                String[] split = jsonFieldPath.split(regexSafeDelimiter);
                int lastIndex = split.length - 1;

                GsonTree currentFieldTree = gsonPathFieldTree;
                for (int i = 0; i < lastIndex + 1; i++) {
                    String currentKey = split[i];

                    if (i < lastIndex) {
                        Object o = currentFieldTree.get(currentKey);
                        if (o == null) {
                            if (i < lastIndex) {
                                GsonTree newMap = new GsonTree();

                                currentFieldTree.addTreeBranch(currentKey, newMap);
                                currentFieldTree = newMap;
                            }
                        } else {
                            if (o instanceof GsonTree) {
                                currentFieldTree = (GsonTree) o;

                            } else {
                                // If this value already exists, and it is not a tree branch, that means we have an invalid duplicate.
                                throwDuplicateFieldException(fieldInfo.getElement(), currentKey);
                            }
                        }

                    } else {
                        // We have reached the end of this branch, add the field at the end.
                        if (!currentFieldTree.containsKey(currentKey)) {
                            currentFieldTree.addField(currentKey, new GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired));

                        } else {
                            throwDuplicateFieldException(fieldInfo.getElement(), currentKey);
                        }
                    }
                }

            } else {
                if (!gsonPathFieldTree.containsKey(jsonFieldPath)) {
                    gsonPathFieldTree.addField(jsonFieldPath, new GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired));

                } else {
                    throwDuplicateFieldException(fieldInfo.getElement(), jsonFieldPath);
                }
            }

        }
        return absoluteRootFieldTree;
    }

    public GsonTree createGsonTreeFromRootField(GsonTree rootFieldTree, String rootField, char flattenDelimiter) {
        if (rootField.length() == 0) {
            return rootFieldTree;
        }

        String regexSafeDelimiter = Pattern.quote(String.valueOf(flattenDelimiter));
        String[] split = rootField.split(regexSafeDelimiter);

        if (split.length > 0) {
            // Keep adding branches to the tree and switching our root to the new branch.
            for (String field : split) {
                GsonTree currentTree = new GsonTree();
                rootFieldTree.addTreeBranch(field, currentTree);
                rootFieldTree = currentTree;
            }

            return rootFieldTree;

        } else {
            // Add a single branch to the tree and return the new branch.
            GsonTree mapWithRoot = new GsonTree();
            rootFieldTree.addTreeBranch(rootField, mapWithRoot);
            return mapWithRoot;
        }
    }

    public List<GsonField> getFlattenedFieldsFromTree(GsonTree rootTree) {
        List<GsonField> flattenedFields = new ArrayList<>();
        getFlattenedFields(rootTree, flattenedFields);

        Collections.sort(flattenedFields, new Comparator<GsonField>() {
            @Override
            public int compare(GsonField o1, GsonField o2) {
                return Integer.compare(o1.fieldIndex, o2.fieldIndex);
            }
        });

        return flattenedFields;
    }

    private void getFlattenedFields(GsonTree currentTree, List<GsonField> flattenedFields) {
        for (String key : currentTree.keySet()) {
            Object value = currentTree.get(key);
            if (value instanceof GsonField) {
                flattenedFields.add((GsonField) value);

            } else {
                GsonTree nextLevelMap = (GsonTree) value;
                if (nextLevelMap.size() > 0) {
                    getFlattenedFields(nextLevelMap, flattenedFields);
                }
            }
        }
    }

    /**
     * Applies the gson field naming policy using the given field name.
     *
     * @param fieldNamingPolicy the field naming policy to apply
     * @param fieldName         the name being altered.
     * @return the altered name.
     */
    @SuppressWarnings("unchecked")
    private String applyFieldNamingPolicy(FieldNamingPolicy fieldNamingPolicy, String fieldName) throws ProcessingException {
        //
        // Unfortunately the field naming policy uses a Field parameter to translate name.
        // As a result, for now it was decided to create a fake field class which supplies the correct name,
        // as opposed to copying the logic from GSON and potentially breaking compatibility if they add another enum.
        //
        Constructor<Field> fieldConstructor = (Constructor<Field>) Field.class.getDeclaredConstructors()[0];
        fieldConstructor.setAccessible(true);
        Field fakeField;
        try {
            fakeField = fieldConstructor.newInstance(null, fieldName, null, -1, -1, null, null);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProcessingException("Error while creating 'fake' field for naming policy.");
        }

        // Applies the naming transformation on the input field name.
        return fieldNamingPolicy.translateName(fakeField);
    }

    private void throwDuplicateFieldException(Element field, String jsonKey) throws ProcessingException {
        throw new ProcessingException("Unexpected duplicate field '" + jsonKey +
                "' found. Each tree branch must use a unique value!", field);
    }
}
