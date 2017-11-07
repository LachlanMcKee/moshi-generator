package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class UsingConstructorTest : BaseGeneratorTest() {
    @Test
    fun testDataClassJavaRepresentation() {
        assertGeneratedContent(TestCriteria("generator/standard/using_constructor/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "ValidConstructorTest.java"),

                relativeGeneratedNames = listOf(
                        "ValidConstructorTest_GsonTypeAdapter.java")
        ))
    }
}
