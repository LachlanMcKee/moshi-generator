package gsonpath.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.ProcessingException
import gsonpath.model.FieldInfoTestFactory
import gsonpath.model.FieldType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AdapterFieldInfoRequiredDetectorTest(
        private val validationResult: GsonObjectValidator.Result,
        private val gsonFieldValidationType: GsonFieldValidationType,
        private val fieldType: FieldType,
        private val isRequired: Boolean) : BaseGsonObjectFactoryTest() {

    private val gsonObjectValidator: GsonObjectValidator = mock()
    private val detector = AdapterFieldInfoRequiredDetector(gsonObjectValidator)

    @Test
    @Throws(ProcessingException::class)
    fun test() {
        // when
        val fieldInfo = FieldInfoTestFactory.mockFieldInfo(DEFAULT_VARIABLE_NAME)
        whenever(fieldInfo.fieldType).thenReturn(fieldType)

        whenever(gsonObjectValidator.validate(fieldInfo)).thenReturn(validationResult)

        val metadata = createMetadata(gsonFieldValidationType = gsonFieldValidationType)

        // when
        val actualIsRequired = detector.isRequired(fieldInfo, metadata)

        // then
        Assert.assertEquals(isRequired, actualIsRequired)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    // Test 'NonNull' annotation permutations with a non-primitive type
                    arrayOf(GsonObjectValidator.Result.Mandatory, GsonFieldValidationType.NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                    arrayOf(GsonObjectValidator.Result.Mandatory, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), true),
                    arrayOf(GsonObjectValidator.Result.Mandatory, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), true),

                    // Test 'Nullable' annotation permutations with a non-primitive type
                    arrayOf(GsonObjectValidator.Result.Optional, GsonFieldValidationType.NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                    arrayOf(GsonObjectValidator.Result.Optional, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), false),
                    arrayOf(GsonObjectValidator.Result.Optional, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), false),

                    // Test no annotation permutations with a non-primitive type
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), true),
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), false),

                    // Test no annotation permutations with a primitive type
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.NO_VALIDATION, FieldType.Primitive(TypeName.INT, mock()), false),
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Primitive(TypeName.INT, mock()), true),
                    arrayOf(GsonObjectValidator.Result.Standard, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, FieldType.Primitive(TypeName.INT, mock()), true)
            )
        }
    }
}
