package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldValidationTypeTest : BaseGeneratorTest() {
    @Test
    fun testValidateExplicitNonNull() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_policy/validate_explicit_non_null")
                .addRelativeSource("TestValidateExplicitNonNull.java")
                .addRelativeGenerated("TestValidateExplicitNonNull_GsonTypeAdapter.java"))
    }

    @Test
    fun testValidateAllExceptNullable() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_policy/validate_all_except_nullable")
                .addRelativeSource("TestValidateAllExceptNullable.java")
                .addRelativeGenerated("TestValidateAllExceptNullable_GsonTypeAdapter.java"))
    }

    @Test
    fun testNoValidation() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_policy/no_validation")
                .addRelativeSource("TestNoValidation.java")
                .addRelativeGenerated("TestNoValidation_GsonTypeAdapter.java"))
    }
}