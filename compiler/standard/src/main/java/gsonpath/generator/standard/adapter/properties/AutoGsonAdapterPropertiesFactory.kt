package gsonpath.generator.standard.adapter.properties

import gsonpath.AutoGsonAdapter
import gsonpath.GsonFieldValidationType
import gsonpath.ProcessingException
import javax.lang.model.element.TypeElement

class AutoGsonAdapterPropertiesFactory {

    @Throws(ProcessingException::class)
    fun create(
            modelElement: TypeElement,
            autoGsonAnnotation: AutoGsonAdapter,
            isInterface: Boolean): AutoGsonAdapterProperties {

        val gsonFieldValidationType =
                if (isInterface) {
                    // Interfaces must use field validation to prevent issues with primitives.
                    when (autoGsonAnnotation.fieldValidationType) {
                        GsonFieldValidationType.NO_VALIDATION ->
                            GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL

                        else ->
                            autoGsonAnnotation.fieldValidationType
                    }
                } else {
                    autoGsonAnnotation.fieldValidationType
                }

        val pathSubstitutions = autoGsonAnnotation.substitutions

        // Validate the path substitutions. Duplicate keys are not allowed.
        pathSubstitutions.fold(emptySet()) { set: Set<String>, pathSubstitution ->
            if (set.contains(pathSubstitution.original)) {
                throw ProcessingException("PathSubstitution original values must be unique", modelElement)
            }
            set.plus(pathSubstitution.original)
        }

        return AutoGsonAdapterProperties(
                autoGsonAnnotation.ignoreNonAnnotatedFields,
                autoGsonAnnotation.flattenDelimiter,
                autoGsonAnnotation.serializeNulls,
                autoGsonAnnotation.rootField,
                gsonFieldValidationType,
                autoGsonAnnotation.fieldNamingPolicy,
                pathSubstitutions)
    }

}