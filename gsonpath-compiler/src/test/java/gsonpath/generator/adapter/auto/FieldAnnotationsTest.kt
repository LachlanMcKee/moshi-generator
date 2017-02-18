package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldAnnotationsTest : BaseGeneratorTest() {

    @Test
    fun testFlattenJson() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_annotations/flatten_json")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestFlattenJson.java")
                .addRelativeGenerated("TestFlattenJson_GsonTypeAdapter.java"))
    }

    @Test
    fun testExcludeFields() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_annotations/exclude")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestExclude.java")
                .addRelativeGenerated("TestExclude_GsonTypeAdapter.java"))
    }
}
