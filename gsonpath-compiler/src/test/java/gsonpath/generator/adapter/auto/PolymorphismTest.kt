package gsonpath.generator.adapter.auto

import com.google.common.truth.Truth
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class PolymorphismTest : BaseGeneratorTest() {
    @Test
    fun givenStringKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/string_keys")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type1.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type2.java")
                .addRelativeSource("TypesList.java")
                .addRelativeGenerated("TypesList_GsonTypeAdapter.java"))
    }

    @Test
    fun givenIntegerKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/integer_keys")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type1.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type2.java")
                .addRelativeSource("TypesList.java")
                .addRelativeGenerated("TypesList_GsonTypeAdapter.java"))
    }

    @Test
    fun givenBooleanKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/boolean_keys")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type1.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type2.java")
                .addRelativeSource("TypesList.java")
                .addRelativeGenerated("TypesList_GsonTypeAdapter.java"))
    }

    @Test
    fun givenStringKeysWithInterface_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/using_interface")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type1.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type2.java")
                .addRelativeSource("TypesList.java")
                .addRelativeGenerated("TypesList_GsonTypeAdapter.java"))
    }

    @Test
    fun givenStringKeysWithListField_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/using_list")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type1.java")
                .addAbsoluteSource("adapter/auto/polymorphism/Type2.java")
                .addRelativeSource("TypesList.java")
                .addRelativeGenerated("TypesList_GsonTypeAdapter.java"))
    }

    @Test
    fun givenNoKeys_whenProcessorExecuted_expectNoKeysError() {
        assertPolymorphismFailure("no_keys", "Gson Path: Keys must be specified for the GsonSubType")
    }

    @Test
    fun givenMultipleKeys_whenProcessorExecuted_expectMultipleKeysError() {
        assertPolymorphismFailure("multiple_keys", "Only one keys array (string, integer or boolean) may be specified for the GsonSubType")
    }

    @Test
    fun givenBlankFieldName_whenProcessorExecuted_expectBlankFieldNameError() {
        assertPolymorphismFailure("blank_field", "Gson Path: fieldName cannot be blank for GsonSubType")
    }

    @Test
    fun givenNoInheritanceLink_whenProcessorExecuted_expectBlankFieldNameError() {
        assertPolymorphismFailure("type_no_inheritance", "Gson Path: subtype java.lang.String does not inherit from adapter.auto.polymorphism.Type")
    }

    private fun assertPolymorphismFailure(directoryName: String, errorMessage: String) {
        val criteria = BaseGeneratorTest.TestCriteria("adapter/auto/polymorphism/$directoryName")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TypesList.java")

        val sourceFilesSize = criteria.sourceFilesSize
        val sources = (0..sourceFilesSize - 1).map { criteria.getSourceFileObject(it) }

        Truth.assertAbout(JavaSourcesSubjectFactory.javaSources()).that(sources)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining(errorMessage)
    }
}