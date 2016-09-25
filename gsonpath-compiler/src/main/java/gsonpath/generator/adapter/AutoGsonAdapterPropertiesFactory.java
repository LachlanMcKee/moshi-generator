package gsonpath.generator.adapter;

import com.google.gson.FieldNamingPolicy;
import gsonpath.*;
import gsonpath.model.GsonFieldNamingPolicyFactory;

import java.util.HashSet;
import java.util.Set;

class AutoGsonAdapterPropertiesFactory {

    AutoGsonAdapterProperties create(AutoGsonAdapter autoGsonAnnotation, GsonPathDefaultConfiguration defaultsAnnotation,
                                     boolean isInterface) throws ProcessingException {

        boolean fieldsRequireAnnotation = autoGsonAnnotation.ignoreNonAnnotatedFields().booleanValue;
        boolean serializeNulls = autoGsonAnnotation.serializeNulls().booleanValue;
        char flattenDelimiter = autoGsonAnnotation.flattenDelimiter().value();
        GsonFieldValidationType gsonFieldValidationType = autoGsonAnnotation.fieldValidationType();
        PathSubstitution[] pathSubstitutions = autoGsonAnnotation.substitutions();

        // Validate the path substitutions. Duplicate keys are not allowed.
        Set<String> pathSubstitutionKeys = new HashSet<>(pathSubstitutions.length);
        for (PathSubstitution pathSubstitution : pathSubstitutions) {
            if (pathSubstitutionKeys.contains(pathSubstitution.original())) {
                throw new ProcessingException("PathSubstitution original values must be unique");
            }
            pathSubstitutionKeys.add(pathSubstitution.original());
        }

        // We want to translate the Gson Path 'GsonPathFieldNamingPolicy' enum into the standard Gson version.
        FieldNamingPolicy gsonFieldNamingPolicy = new GsonFieldNamingPolicyFactory().getPolicy(autoGsonAnnotation.fieldNamingPolicy());

        if (defaultsAnnotation != null) {

            // Inherit 'ignoreNonAnnotatedFields'
            if (autoGsonAnnotation.ignoreNonAnnotatedFields().inheritDefaultIfAvailable) {
                fieldsRequireAnnotation = defaultsAnnotation.ignoreNonAnnotatedFields();
            }

            // Inherit 'serializeNulls'
            if (autoGsonAnnotation.serializeNulls().inheritDefaultIfAvailable) {
                serializeNulls = defaultsAnnotation.serializeNulls();
            }

            // Inherit 'flattenDelimiter'
            if (autoGsonAnnotation.flattenDelimiter().inheritDefaultIfAvailable()) {
                flattenDelimiter = defaultsAnnotation.flattenDelimiter();
            }

            // Inherit 'fieldNamingPolicy'
            if (autoGsonAnnotation.fieldNamingPolicy().equals(GsonPathFieldNamingPolicy.IDENTITY_OR_INHERIT_DEFAULT_IF_AVAILABLE)) {
                gsonFieldNamingPolicy = defaultsAnnotation.fieldNamingPolicy();
            }

            // Inherit 'fieldValidationType'
            if (gsonFieldValidationType.equals(GsonFieldValidationType.NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE)) {
                gsonFieldValidationType = defaultsAnnotation.fieldValidationType();
            }
        }

        // Interfaces must use field validation to prevent issues with primitives.
        if (isInterface) {
            switch (gsonFieldValidationType) {
                case NO_VALIDATION:
                case NO_VALIDATION_OR_INHERIT_DEFAULT_IF_AVAILABLE:
                    gsonFieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL;
                    break;
            }
        }

        return new AutoGsonAdapterProperties(fieldsRequireAnnotation, flattenDelimiter, serializeNulls,
                autoGsonAnnotation.rootField(), gsonFieldValidationType, gsonFieldNamingPolicy, pathSubstitutions);
    }

}
