package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class CustomAutoGsonAdapterTest : BaseGeneratorTest() {
    @Test
    fun testCustomAutoGsonAdapterAnnotation() {
        assertGeneratedContent(TestCriteria("generator/standard/custom_annotation",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "CustomAutoGsonAdapter.java",
                        "TestCustomAutoGsonAdapterModel.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java")
        ))
    }
}
