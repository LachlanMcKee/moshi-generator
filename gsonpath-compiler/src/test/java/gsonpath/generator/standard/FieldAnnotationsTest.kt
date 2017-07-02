package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class FieldAnnotationsTest : BaseGeneratorTest() {

    @Test
    fun testFlattenJson() {
        assertGeneratedContent(TestCriteria("generator/standard/field_annotations/flatten_json",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestFlattenJson.java"),

                relativeGeneratedNames = listOf(
                        "TestFlattenJson_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testExcludeFields() {
        assertGeneratedContent(TestCriteria("generator/standard/field_annotations/exclude",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestExclude.java"),

                relativeGeneratedNames = listOf(
                        "TestExclude_GsonTypeAdapter.java")
        ))
    }
}
