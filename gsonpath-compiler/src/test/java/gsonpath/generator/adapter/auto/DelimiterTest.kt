package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class DelimiterTest : BaseGeneratorTest() {

    @Test
    fun testStandardDelimiter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/delimiter/standard")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestStandardDelimiter.java")
                .addRelativeGenerated("TestStandardDelimiter_GsonTypeAdapter.java"))
    }

    @Test
    fun testCustomDelimiter() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/delimiter/custom")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestCustomDelimiter.java")
                .addRelativeGenerated("TestCustomDelimiter_GsonTypeAdapter.java"))
    }

    @Test
    fun testMultipleDelimiters() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/delimiter/multiple")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestMultipleDelimiters.java")
                .addRelativeGenerated("TestMultipleDelimiters_GsonTypeAdapter.java"))
    }

}
