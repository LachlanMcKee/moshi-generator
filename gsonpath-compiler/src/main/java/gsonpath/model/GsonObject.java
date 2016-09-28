package gsonpath.model;

import com.google.common.base.Objects;

import java.util.LinkedHashMap;

public class GsonObject {
    private final LinkedHashMap<String, Object> fieldMap;

    public GsonObject() {
        fieldMap = new LinkedHashMap<>();
    }

    public GsonObject addObject(String branchName, GsonObject gsonObject) {
        fieldMap.put(branchName, gsonObject);
        return gsonObject;
    }

    public GsonField addField(String branchName, GsonField field) throws IllegalArgumentException {
        if (containsKey(branchName)) {
            throw new IllegalArgumentException("Value already exists");
        }
        fieldMap.put(branchName, field);
        return field;
    }

    public int size() {
        return fieldMap.size();
    }

    public java.util.Set<String> keySet() {
        return fieldMap.keySet();
    }

    public Object get(String key) {
        return fieldMap.get(key);
    }

    public boolean containsKey(String key) {
        return fieldMap.containsKey(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GsonObject gsonObject = (GsonObject) o;
        return Objects.equal(fieldMap, gsonObject.fieldMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldMap);
    }

    @Override
    public String toString() {
        return "GsonObject: " + fieldMap;
    }
}
