package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class NestedClassTest : BaseGeneratorTest() {
    @Test
    fun testNestedClasses() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_class",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNestedClass.java"),

                relativeGeneratedNames = listOf(
                        "TestNestedClass_Nested_GsonTypeAdapter.java")
        ))
    }
}
