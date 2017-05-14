package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class NestedClassTest : BaseGeneratorTest() {
    @Test
    fun testNestedClasses() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_class")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestNestedClass.java")
                .addRelativeGenerated("TestNestedClass_Nested_GsonTypeAdapter.java"))
    }
}
