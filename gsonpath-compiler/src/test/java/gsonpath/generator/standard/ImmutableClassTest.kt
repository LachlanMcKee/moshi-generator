package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class ImmutableClassTest : BaseGeneratorTest() {
    @Test
    fun testAutoGsonAdapterOnly() {
        assertGeneratedContent(TestCriteria("generator/standard/immutable_class",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "DataClassTest.java"),

                relativeGeneratedNames = listOf(
                        "DataClassTest_GsonTypeAdapter.java")
        ))
    }
}
