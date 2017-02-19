package gsonpath.generator.adapter.auto

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
}