package gsonpath.model;

import com.google.common.base.Objects;

import java.util.LinkedHashMap;

public class GsonTree {
    private final LinkedHashMap<String, Object> fieldMap;

    public GsonTree() {
        fieldMap = new LinkedHashMap<>();
    }

    public void addTreeBranch(String branchName, GsonTree gsonTree) {
        fieldMap.put(branchName, gsonTree);
    }

    public void addField(String branchName, GsonField field) throws IllegalArgumentException {
        if (containsKey(branchName)) {
            throw new IllegalArgumentException("Value already exists");
        }
        fieldMap.put(branchName, field);
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

        GsonTree gsonTree = (GsonTree) o;
        return Objects.equal(fieldMap, gsonTree.fieldMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldMap);
    }

    @Override
    public String toString() {
        return "GsonTree{" +
                "fieldMap=" + fieldMap +
                '}';
    }
}
