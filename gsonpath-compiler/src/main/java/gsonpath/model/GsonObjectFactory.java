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
import java.util.regex.Pattern;

public class GsonObjectFactory {
    public void addGsonType(GsonObject gsonPathObject,
                            FieldInfo fieldInfo,
                            int fieldInfoIndex,
                            char flattenDelimiter,
                            FieldNamingPolicy gsonFieldNamingPolicy,
                            GsonFieldValidationType gsonFieldValidationType,
                            PathSubstitution[] pathSubstitutions) throws ProcessingException {

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
            addNestedType(gsonPathObject, fieldInfo, jsonFieldPath, flattenDelimiter, fieldInfoIndex, isRequired,
                    fieldName);

        } else {
            addStandardType(gsonPathObject, fieldInfo, jsonFieldPath, fieldInfoIndex, isRequired);
        }
    }

    private void addNestedType(GsonObject gsonPathObject,
                               FieldInfo fieldInfo,
                               String jsonFieldPath,
                               char flattenDelimiter,
                               int fieldInfoIndex,
                               boolean isRequired,
                               String fieldName) throws ProcessingException {
        //
        // When the last character is a delimiter, we should append the variable name to
        // the end of the field name, as this may reduce annotation repetition.
        //
        if (jsonFieldPath.charAt(jsonFieldPath.length() - 1) == flattenDelimiter) {
            jsonFieldPath += fieldName;
        }

        // Ensure that the delimiter is correctly escaped before attempting to pathSegments the string.
        String regexSafeDelimiter = Pattern.quote(String.valueOf(flattenDelimiter));
        String[] pathSegments = jsonFieldPath.split(regexSafeDelimiter);

        int lastPathIndex = pathSegments.length - 1;
        Object currentGsonType = gsonPathObject;

        for (int currentSegmentIndex = 0; currentSegmentIndex < lastPathIndex + 1; currentSegmentIndex++) {
            String pathSegment = pathSegments[currentSegmentIndex];

            if (currentSegmentIndex < lastPathIndex) {

                if (currentGsonType.getClass().equals(GsonObject.class)) {
                    GsonObject currentGsonObject = (GsonObject) currentGsonType;
                    Object o = currentGsonObject.get(pathSegment);

                    if (o != null) {
                        if (o.getClass().equals(GsonObject.class)) {
                            currentGsonType = o;

                        } else {
                            // If this value already exists, and it is not a tree branch, that means we have an invalid duplicate.
                            throwDuplicateFieldException(fieldInfo.getElement(), pathSegment);
                        }
                    } else {
                        GsonObject newMap = new GsonObject();
                        currentGsonObject.addObject(pathSegment, newMap);
                        currentGsonType = newMap;
                    }
                }

            } else {
                // We have reached the end of this object branch, add the field at the end.
                try {
                    GsonField field = new GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired);
                    ((GsonObject) currentGsonType).addField(pathSegment, field);

                } catch (IllegalArgumentException e) {
                    throwDuplicateFieldException(fieldInfo.getElement(), pathSegment);
                }
            }
        }
    }

    private void addStandardType(GsonObject gsonPathObject,
                                 FieldInfo fieldInfo,
                                 String jsonFieldPath,
                                 int fieldInfoIndex,
                                 boolean isRequired) throws ProcessingException {

        if (!gsonPathObject.containsKey(jsonFieldPath)) {
            gsonPathObject.addField(jsonFieldPath, new GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired));

        } else {
            throwDuplicateFieldException(fieldInfo.getElement(), jsonFieldPath);
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
