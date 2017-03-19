package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldValidationTypeTest : BaseGeneratorTest() {
    @Test
    fun testValidateExplicitNonNull() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/validate_explicit_non_null")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestValidateExplicitNonNull.java")
                .addRelativeGenerated("TestValidateExplicitNonNull_GsonTypeAdapter.java"))
    }

    @Test
    fun testValidateAllExceptNullable() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/validate_all_except_nullable")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestValidateAllExceptNullable.java")
                .addRelativeGenerated("TestValidateAllExceptNullable_GsonTypeAdapter.java"))
    }

    @Test
    fun testNoValidation() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/no_validation")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNoValidation.java")
                .addRelativeGenerated("TestNoValidation_GsonTypeAdapter.java"))
    }
}