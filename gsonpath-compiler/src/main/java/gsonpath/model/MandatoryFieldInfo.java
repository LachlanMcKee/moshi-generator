package gsonpath.model;

/**
 * Keeps track of mandatory json field metadata.
 */
public class MandatoryFieldInfo {
    public final String indexVariableName;
    public final GsonField gsonField;

    MandatoryFieldInfo(String indexVariableName, GsonField gsonField) {
        this.indexVariableName = indexVariableName;
        this.gsonField = gsonField;
    }
}
