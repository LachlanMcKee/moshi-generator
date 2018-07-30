package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class UseGetterAnnotationTest : BaseGeneratorTest() {
    @Test
    fun testDataClassJavaRepresentation() {
        assertGeneratedContent(TestCriteria("generator/standard/use_getter_annotation/",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "UseGetterAnnotationTest.java"),

                relativeGeneratedNames = listOf(
                        "UseGetterAnnotationTest_Implementation_GsonTypeAdapter.java")
        ))
    }
}
