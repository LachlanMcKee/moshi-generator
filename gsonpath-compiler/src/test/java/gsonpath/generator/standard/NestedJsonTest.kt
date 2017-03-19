package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class NestedJsonTest : BaseGeneratorTest() {
    @Test
    fun testFlatteningUsingFields() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/field_nesting")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestFieldNesting.java")
                .addRelativeGenerated("TestFieldNesting_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlatteningUsingRootField() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/root_nesting")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestRootNesting.java")
                .addRelativeGenerated("TestRootNesting_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlatteningUsingFieldAutoComplete() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/field_nesting_autocomplete")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestFieldNestingAutocomplete.java")
                .addRelativeGenerated("TestFieldNestingAutocomplete_GsonTypeAdapter.java"))
    }
}
