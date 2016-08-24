package gsonpath.generator;

public class FieldPathInfo {
    public final int fieldIndex;
    public final FieldInfo fieldInfo;
    public final String jsonPath;
    public final boolean isRequired;

    public FieldPathInfo(int fieldIndex, FieldInfo fieldInfo, String jsonPath, boolean isRequired) {
        this.fieldIndex = fieldIndex;
        this.fieldInfo = fieldInfo;
        this.jsonPath = jsonPath;
        this.isRequired = isRequired;
    }

    public String getVariableName() {
        return "value_" + jsonPath.replaceAll("[^A-Za-z0-9_]", "_");
    }
}
