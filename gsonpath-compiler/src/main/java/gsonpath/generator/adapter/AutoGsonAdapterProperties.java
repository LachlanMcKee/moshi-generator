package gsonpath.generator.adapter;

import com.google.gson.FieldNamingPolicy;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;

class AutoGsonAdapterProperties {
    final boolean fieldsRequireAnnotation;
    final char flattenDelimiter;
    final boolean serializeNulls;
    final String rootField;
    final GsonFieldValidationType gsonFieldValidationType;
    final FieldNamingPolicy gsonFieldNamingPolicy;
    final PathSubstitution[] pathSubstitutions;

    AutoGsonAdapterProperties(boolean fieldsRequireAnnotation, char flattenDelimiter, boolean serializeNulls,
                              String rootField, GsonFieldValidationType gsonFieldValidationType,
                              FieldNamingPolicy gsonFieldNamingPolicy, PathSubstitution[] pathSubstitutions) {

        this.fieldsRequireAnnotation = fieldsRequireAnnotation;
        this.flattenDelimiter = flattenDelimiter;
        this.serializeNulls = serializeNulls;
        this.rootField = rootField;
        this.gsonFieldValidationType = gsonFieldValidationType;
        this.gsonFieldNamingPolicy = gsonFieldNamingPolicy;
        this.pathSubstitutions = pathSubstitutions;
    }
}
