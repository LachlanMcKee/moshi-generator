package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class ArrayTest : BaseGeneratorTest() {

    @Test
    fun testArray() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("generator/standard/array",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestArray.java"),
                relativeGeneratedNames = listOf("TestArray_GsonTypeAdapter.java")
        ))
    }
}
