package gsonpath.generator.standard

import com.google.gson.FieldNamingPolicy
import gsonpath.*
import gsonpath.ProcessingException
import gsonpath.model.GsonFieldNamingPolicyFactory

import java.util.HashSet

internal class AutoGsonAdapterPropertiesFactory {

    @Throws(ProcessingException::class)
    fun create(autoGsonAnnotation: AutoGsonAdapter, defaultsAnnotation: GsonPathDefaultConfiguration?,
               isInterface: Boolean): AutoGsonAdapterProperties {

        var fieldsRequireAnnotation = autoGsonAnnotation.ignoreNonAnnotatedFields.booleanValue
        var serializeNulls = autoGsonAnnotation.serializeNulls.booleanValue
        var flattenDelimiter = autoGsonAnnotation.flattenDelimiter.value
        var gsonFieldValidationType = autoGsonAnnotation.fieldValidationType
        val pathSubstitutions = autoGsonAnnotation.substitutions

        // Validate the path substitutions. Duplicate keys are not allowed.
        val pathSubstitutionKeys = HashSet<String>(pathSubstitutions.size)
        for (pathSubstitution in pathSubstitutions) {
            if (pathSubstitutionKeys.contains(pathSubstitution.original)) {
                throw ProcessingException("PathSubstitution original values must be unique")
            }
            pathSubstitutionKeys.add(pathSubstitution.original)
        }

        // We want to translate the Gson Path 'GsonPathFieldNamingPolicy' enum into the standard Gson version.
        var gsonFieldNamingPolicy: FieldNamingPolicy = GsonFieldNamingPolicyFactory().getPolicy(autoGsonAnnotation.fieldNamingPolicy)

        if (defaultsAnnotation != null) {

            // Inherit 'ignoreNonAnnotatedFields'
            if (autoGsonAnnotation.ignoreNonAnnotatedFields.inheritDefaultIfAvailable) {
                fieldsRequireAnnotation = defaultsAnnotation.ignoreNonAnnotatedFields
            }

            // Inherit 'serializeNulls'
            if (autoGsonAnnotation.serializeNulls.inheritDefaultIfAvailable) {
                serializeNulls = defaultsAnnotation.serializeNulls
            }

            // Inherit 'flattenDelimiter'
            if (autoGsonAnnotation.flattenDelimiter.inheritDefaultIfAvailable) {
                flattenDelimiter = defaultsAnnotation.flattenDelimiter
            }

            // Inherit 'fieldNamingPolicy'
            if (autoGsonAnnotation.fieldNamingPolicy == GsonPathFieldNamingPolicy.IDENTITY_OR_INHERIT_DEFAULT_IF_AVAILABLE) {
                gsonFieldNamingPolicy = defaultsAnnotation.fieldNamingPolicy
            }

            // Inherit 'fieldValidationType'
            if (gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE) {
                gsonFieldValidationType = defaultsAnnotation.fieldValidationType
            }
        }

        // Interfaces must use field validation to prevent issues with primitives.
        if (isInterface) {
            when (gsonFieldValidationType) {
                GsonFieldValidationType.NO_VALIDATION,
                GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE ->
                    gsonFieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL
            }
        }

        return AutoGsonAdapterProperties(fieldsRequireAnnotation, flattenDelimiter, serializeNulls,
                autoGsonAnnotation.rootField, gsonFieldValidationType, gsonFieldNamingPolicy, pathSubstitutions)
    }

}
