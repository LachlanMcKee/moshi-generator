package gsonpath.adapter.standard.model

import gsonpath.GsonFieldValidationType
import gsonpath.model.AdapterFieldInfo
import gsonpath.model.FieldType

class AdapterFieldInfoRequiredDetector(private val gsonObjectValidator: GsonObjectValidator) : FieldInfoRequiredDetector<AdapterFieldInfo> {
    override fun isRequired(fieldInfo: AdapterFieldInfo, metadata: GsonObjectMetadata): Boolean {
        val isPrimitive = fieldInfo.fieldType is FieldType.Primitive
        val validationResult = gsonObjectValidator.validate(fieldInfo)
        return when {
            validationResult == GsonObjectValidator.Result.Optional ->
                // Optionals will never fail regardless of the policy.
                false

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE ->
                // Using this policy everything is mandatory except for optionals.
                !fieldInfo.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL && isPrimitive ->
                // Primitives are treated as non-null implicitly.
                !fieldInfo.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION ->
                false

            else ->
                validationResult == GsonObjectValidator.Result.Mandatory && !fieldInfo.hasDefaultValue
        }
    }
}