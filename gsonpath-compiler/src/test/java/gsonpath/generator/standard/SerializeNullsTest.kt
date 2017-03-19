package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class SerializeNullsTest : BaseGeneratorTest() {

    @Test
    fun testSerializeNulls() {
        assertGeneratedContent(TestCriteria("generator/standard/serialize_nulls")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestSerializeNulls.java")
                .addRelativeGenerated("TestSerializeNulls_GsonTypeAdapter.java"))
    }

}
