package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class DelimiterTest : BaseGeneratorTest() {

    @Test
    fun testStandardDelimiter() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/standard")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestStandardDelimiter.java")
                .addRelativeGenerated("TestStandardDelimiter_GsonTypeAdapter.java"))
    }

    @Test
    fun testCustomDelimiter() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/custom")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestCustomDelimiter.java")
                .addRelativeGenerated("TestCustomDelimiter_GsonTypeAdapter.java"))
    }

    @Test
    fun testMultipleDelimiters() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/multiple")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestMultipleDelimiters.java")
                .addRelativeGenerated("TestMultipleDelimiters_GsonTypeAdapter.java"))
    }

}
