package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldNamingPolicyTest : BaseGeneratorTest() {
    @Test
    fun testIdentity() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/naming_policy/identity")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyIdentity.java")
                .addRelativeGenerated("TestNamePolicyIdentity_GsonTypeAdapter.java"))
    }

    @Test
    fun testLowerCaseWithDashes() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/naming_policy/lowercase_dashes")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyLowerCaseDashes.java")
                .addRelativeGenerated("TestNamePolicyLowerCaseDashes_GsonTypeAdapter.java"))
    }

    @Test
    fun testLowerCaseWithUnderscores() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/naming_policy/lowercase_underscores")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyLowerCaseUnderscores.java")
                .addRelativeGenerated("TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter.java"))
    }

    @Test
    fun testUpperCamelCase() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/naming_policy/upper_camel_case")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyUpperCamelCase.java")
                .addRelativeGenerated("TestNamePolicyUpperCamelCase_GsonTypeAdapter.java"))
    }

    @Test
    fun testUpperCamelCaseWithSpaces() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/naming_policy/upper_camel_case_spaces")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestNamePolicyUpperCamelCaseSpaces.java")
                .addRelativeGenerated("TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter.java"))
    }
}
