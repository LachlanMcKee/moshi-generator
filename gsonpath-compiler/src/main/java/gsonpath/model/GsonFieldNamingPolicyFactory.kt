package gsonpath.model

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonPathFieldNamingPolicy

class GsonFieldNamingPolicyFactory {
    fun getPolicy(gsonPathFieldNamingPolicy: GsonPathFieldNamingPolicy): FieldNamingPolicy {
        when (gsonPathFieldNamingPolicy) {
            GsonPathFieldNamingPolicy.IDENTITY,
            GsonPathFieldNamingPolicy.IDENTITY_OR_INHERIT_DEFAULT_IF_AVAILABLE ->
                return FieldNamingPolicy.IDENTITY

            GsonPathFieldNamingPolicy.LOWER_CASE_WITH_DASHES ->
                return FieldNamingPolicy.LOWER_CASE_WITH_DASHES

            GsonPathFieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES ->
                return FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES

            GsonPathFieldNamingPolicy.UPPER_CAMEL_CASE ->
                return FieldNamingPolicy.UPPER_CAMEL_CASE

            GsonPathFieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES ->
                return FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES
        }
    }
}
