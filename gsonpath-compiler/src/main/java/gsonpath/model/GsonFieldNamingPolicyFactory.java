package gsonpath.model;

import com.google.gson.FieldNamingPolicy;
import gsonpath.GsonPathFieldNamingPolicy;

public class GsonFieldNamingPolicyFactory {
    public FieldNamingPolicy getPolicy(GsonPathFieldNamingPolicy gsonPathFieldNamingPolicy) {
        switch (gsonPathFieldNamingPolicy) {
            case IDENTITY:
            case IDENTITY_OR_INHERIT_DEFAULT_IF_AVAILABLE:
                return FieldNamingPolicy.IDENTITY;

            case LOWER_CASE_WITH_DASHES:
                return FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

            case LOWER_CASE_WITH_UNDERSCORES:
                return FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

            case UPPER_CAMEL_CASE:
                return FieldNamingPolicy.UPPER_CAMEL_CASE;

            case UPPER_CAMEL_CASE_WITH_SPACES:
                return FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;
        }
        return null;
    }
}
