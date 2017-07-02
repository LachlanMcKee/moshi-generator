package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class SerializeNullsTest : BaseGeneratorTest() {

    @Test
    fun testSerializeNulls() {
        assertGeneratedContent(TestCriteria("generator/standard/serialize_nulls",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestSerializeNulls.java"),

                relativeGeneratedNames = listOf(
                        "TestSerializeNulls_GsonTypeAdapter.java")
        ))
    }

}
