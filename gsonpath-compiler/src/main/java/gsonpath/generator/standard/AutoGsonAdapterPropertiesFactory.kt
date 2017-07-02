package gsonpath.generator.standard

import com.google.gson.FieldNamingPolicy
import gsonpath.*
import gsonpath.ProcessingException
import gsonpath.model.GsonFieldNamingPolicyFactory

internal class AutoGsonAdapterPropertiesFactory {

    @Throws(ProcessingException::class)
    fun create(autoGsonAnnotation: AutoGsonAdapter, defaultsAnnotation: GsonPathDefaultConfiguration?,
               isInterface: Boolean): AutoGsonAdapterProperties {

        val fieldsRequireAnnotation =
                if (defaultsAnnotation != null && autoGsonAnnotation.ignoreNonAnnotatedFields.inheritDefaultIfAvailable) {
                    // Inherit 'ignoreNonAnnotatedFields'
                    defaultsAnnotation.ignoreNonAnnotatedFields
                } else {
                    autoGsonAnnotation.ignoreNonAnnotatedFields.booleanValue
                }

        val serializeNulls =
                if (defaultsAnnotation != null && autoGsonAnnotation.serializeNulls.inheritDefaultIfAvailable) {
                    // Inherit 'serializeNulls'
                    defaultsAnnotation.serializeNulls
                } else {
                    autoGsonAnnotation.serializeNulls.booleanValue
                }

        val flattenDelimiter =
                if (defaultsAnnotation != null && autoGsonAnnotation.flattenDelimiter.inheritDefaultIfAvailable) {
                    // Inherit 'flattenDelimiter'
                    defaultsAnnotation.flattenDelimiter
                } else {
                    autoGsonAnnotation.flattenDelimiter.value
                }

        val gsonFieldValidationType =
                if (defaultsAnnotation != null && autoGsonAnnotation.fieldValidationType ==
                        GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE) {

                    if (isInterface) {
                        // Interfaces must use field validation to prevent issues with primitives.
                        when (defaultsAnnotation.fieldValidationType) {
                            GsonFieldValidationType.NO_VALIDATION,
                            GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE ->
                                GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL

                            else ->
                                defaultsAnnotation.fieldValidationType
                        }
                    } else {
                        // Inherit 'fieldValidationType'
                        defaultsAnnotation.fieldValidationType
                    }

                } else {
                    if (isInterface) {
                        // Interfaces must use field validation to prevent issues with primitives.
                        when (autoGsonAnnotation.fieldValidationType) {
                            GsonFieldValidationType.NO_VALIDATION,
                            GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE ->
                                GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL

                            else ->
                                autoGsonAnnotation.fieldValidationType
                        }
                    } else {
                        autoGsonAnnotation.fieldValidationType
                    }
                }

        val pathSubstitutions = autoGsonAnnotation.substitutions

        // Validate the path substitutions. Duplicate keys are not allowed.
        pathSubstitutions.fold(emptySet()) { set: Set<String>, pathSubstitution ->
            if (set.contains(pathSubstitution.original)) {
                throw ProcessingException("PathSubstitution original values must be unique")
            }
            set.plus(pathSubstitution.original)
        }

        // We want to translate the Gson Path 'GsonPathFieldNamingPolicy' enum into the standard Gson version.
        val gsonFieldNamingPolicy: FieldNamingPolicy =
                if (defaultsAnnotation != null && autoGsonAnnotation.fieldNamingPolicy ==
                        GsonPathFieldNamingPolicy.IDENTITY_OR_INHERIT_DEFAULT_IF_AVAILABLE) {

                    // Inherit 'fieldNamingPolicy'
                    defaultsAnnotation.fieldNamingPolicy
                } else {
                    GsonFieldNamingPolicyFactory().getPolicy(autoGsonAnnotation.fieldNamingPolicy)
                }

        return AutoGsonAdapterProperties(fieldsRequireAnnotation, flattenDelimiter, serializeNulls,
                autoGsonAnnotation.rootField, gsonFieldValidationType, gsonFieldNamingPolicy, pathSubstitutions)
    }

}
