package gsonpath.adapter.standard.model

import gsonpath.GsonFieldValidationType
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType

class FieldInfoBahRequiredDetector(private val gsonObjectValidator: GsonObjectValidator) : BahRequiredDetector<FieldInfo> {
    override fun isRequired(bah: FieldInfo, metadata: GsonObjectMetadata): Boolean {
        val isPrimitive = bah.fieldType is FieldType.Primitive
        val validationResult = gsonObjectValidator.validate(bah)
        return when {
            validationResult == GsonObjectValidator.Result.Optional ->
                // Optionals will never fail regardless of the policy.
                false

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE ->
                // Using this policy everything is mandatory except for optionals.
                !bah.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL && isPrimitive ->
                // Primitives are treated as non-null implicitly.
                !bah.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION ->
                false

            else ->
                validationResult == GsonObjectValidator.Result.Mandatory && !bah.hasDefaultValue
        }
    }
}