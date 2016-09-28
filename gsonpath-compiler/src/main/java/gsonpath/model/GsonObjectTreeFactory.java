package gsonpath.model;

import com.google.gson.FieldNamingPolicy;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;
import gsonpath.ProcessingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class GsonObjectTreeFactory {
    public GsonObject createGsonObject(List<FieldInfo> fieldInfoList,
                                       String rootField,
                                       char flattenDelimiter,
                                       FieldNamingPolicy gsonFieldNamingPolicy,
                                       GsonFieldValidationType gsonFieldValidationType,
                                       PathSubstitution[] pathSubstitutions) throws ProcessingException {

        // Obtain the correct mapping structure beforehand.
        GsonObject absoluteRootObject = new GsonObject();
        GsonObject gsonPathObject = absoluteRootObject;

        GsonObjectFactory gsonObjectFactory = new GsonObjectFactory();

        if (rootField.length() > 0) {
            gsonPathObject = createGsonObjectFromRootField(gsonPathObject, rootField, flattenDelimiter);

        } else {
            gsonPathObject = absoluteRootObject;
        }

        for (int fieldInfoIndex = 0; fieldInfoIndex < fieldInfoList.size(); fieldInfoIndex++) {
            gsonObjectFactory.addGsonType(gsonPathObject, fieldInfoList.get(fieldInfoIndex), fieldInfoIndex,
                    flattenDelimiter, gsonFieldNamingPolicy, gsonFieldValidationType, pathSubstitutions);
        }
        return absoluteRootObject;
    }

    public GsonObject createGsonObjectFromRootField(GsonObject rootObject, String rootField, char flattenDelimiter) {
        if (rootField.length() == 0) {
            return rootObject;
        }

        String regexSafeDelimiter = Pattern.quote(String.valueOf(flattenDelimiter));
        String[] split = rootField.split(regexSafeDelimiter);

        if (split.length > 0) {
            // Keep adding branches to the tree and switching our root to the new branch.
            for (String field : split) {
                GsonObject currentObject = new GsonObject();
                rootObject.addObject(field, currentObject);
                rootObject = currentObject;
            }

            return rootObject;

        } else {
            // Add a single branch to the tree and return the new branch.
            GsonObject mapWithRoot = new GsonObject();
            rootObject.addObject(rootField, mapWithRoot);
            return mapWithRoot;
        }
    }

    public List<GsonField> getFlattenedFieldsFromGsonObject(GsonObject gsonObject) {
        List<GsonField> flattenedFields = new ArrayList<>();
        getFlattenedFields(gsonObject, flattenedFields);

        Collections.sort(flattenedFields, new Comparator<GsonField>() {
            @Override
            public int compare(GsonField o1, GsonField o2) {
                return Integer.compare(o1.fieldIndex, o2.fieldIndex);
            }
        });

        return flattenedFields;
    }

    private void getFlattenedFields(GsonObject currentGsonObject, List<GsonField> flattenedFields) {
        for (String key : currentGsonObject.keySet()) {
            Object value = currentGsonObject.get(key);
            if (value.getClass().equals(GsonField.class)) {
                flattenedFields.add((GsonField) value);

            } else {
                GsonObject nextLevelMap = (GsonObject) value;
                if (nextLevelMap.size() > 0) {
                    getFlattenedFields(nextLevelMap, flattenedFields);
                }
            }
        }
    }
}
