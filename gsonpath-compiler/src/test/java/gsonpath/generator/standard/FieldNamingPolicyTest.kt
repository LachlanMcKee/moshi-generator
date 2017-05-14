package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldNamingPolicyTest : BaseGeneratorTest() {
    @Test
    fun testIdentity() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/identity")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyIdentity.java")
                .addRelativeGenerated("TestNamePolicyIdentity_GsonTypeAdapter.java"))
    }

    @Test
    fun testLowerCaseWithDashes() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/lowercase_dashes")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyLowerCaseDashes.java")
                .addRelativeGenerated("TestNamePolicyLowerCaseDashes_GsonTypeAdapter.java"))
    }

    @Test
    fun testLowerCaseWithUnderscores() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/lowercase_underscores")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyLowerCaseUnderscores.java")
                .addRelativeGenerated("TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter.java"))
    }

    @Test
    fun testUpperCamelCase() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/upper_camel_case")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyUpperCamelCase.java")
                .addRelativeGenerated("TestNamePolicyUpperCamelCase_GsonTypeAdapter.java"))
    }

    @Test
    fun testUpperCamelCaseWithSpaces() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/upper_camel_case_spaces")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyUpperCamelCaseSpaces.java")
                .addRelativeGenerated("TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter.java"))
    }
}
