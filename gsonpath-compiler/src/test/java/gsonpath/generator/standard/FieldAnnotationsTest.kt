package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldAnnotationsTest : BaseGeneratorTest() {

    @Test
    fun testFlattenJson() {
        assertGeneratedContent(TestCriteria("generator/standard/field_annotations/flatten_json")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestFlattenJson.java")
                .addRelativeGenerated("TestFlattenJson_GsonTypeAdapter.java"))
    }

    @Test
    fun testExcludeFields() {
        assertGeneratedContent(TestCriteria("generator/standard/field_annotations/exclude")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestExclude.java")
                .addRelativeGenerated("TestExclude_GsonTypeAdapter.java"))
    }
}
