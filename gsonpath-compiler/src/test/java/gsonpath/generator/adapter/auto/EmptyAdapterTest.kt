package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

/**
 * These tests are used whenever the input class file generates a blank type adapter.
 *
 *
 * This can be due to the class being empty, or the content within the class isn't
 * applicable for a type adapter.
 */
class EmptyAdapterTest : BaseGeneratorTest() {

    /**
     * Tests the output generated when only a [gsonpath.AutoGsonAdapter] annotation is used.
     */
    @Test
    fun testAutoGsonAdapterOnly() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/empty/annotation_only")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestAnnotationOnly.java")
                .addRelativeGenerated("TestAnnotationOnly_GsonTypeAdapter.java"))
    }

    @Test
    fun testRequiresAnnotation() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/empty/ignored_fields")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestIgnoredFields.java")
                .addRelativeGenerated("TestIgnoredFields_GsonTypeAdapter.java"))
    }

    @Test
    fun testIgnoreInvalidFields() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/empty/invalid_fields")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestInvalidFields.java")
                .addRelativeGenerated("TestInvalidFields_GsonTypeAdapter.java"))
    }
}
