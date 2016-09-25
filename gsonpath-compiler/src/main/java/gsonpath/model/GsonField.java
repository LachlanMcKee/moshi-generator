package gsonpath.model;

import com.google.common.base.Objects;

public class GsonField {
    public final int fieldIndex;
    public final FieldInfo fieldInfo;
    public final String jsonPath;
    public final boolean isRequired;

    public GsonField(int fieldIndex, FieldInfo fieldInfo, String jsonPath, boolean isRequired) {
        this.fieldIndex = fieldIndex;
        this.fieldInfo = fieldInfo;
        this.jsonPath = jsonPath;
        this.isRequired = isRequired;
    }

    public String getVariableName() {
        return "value_" + jsonPath.replaceAll("[^A-Za-z0-9_]", "_");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GsonField gsonField = (GsonField) o;
        return fieldIndex == gsonField.fieldIndex &&
                isRequired == gsonField.isRequired &&
                Objects.equal(fieldInfo, gsonField.fieldInfo) &&
                Objects.equal(jsonPath, gsonField.jsonPath);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldIndex, fieldInfo, jsonPath, isRequired);
    }

    @Override
    public String toString() {
        return "GsonField{" +
                "fieldIndex=" + fieldIndex +
                ", fieldInfo=" + fieldInfo +
                ", jsonPath='" + jsonPath + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
