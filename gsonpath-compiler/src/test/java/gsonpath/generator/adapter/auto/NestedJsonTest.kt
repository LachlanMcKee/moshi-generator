package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class NestedJsonTest : BaseGeneratorTest() {
    @Test
    fun testFlatteningUsingFields() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/nested_json/field_nesting")
                .addRelativeSource("TestFieldNesting.java")
                .addRelativeGenerated("TestFieldNesting_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlatteningUsingRootField() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/nested_json/root_nesting")
                .addRelativeSource("TestRootNesting.java")
                .addRelativeGenerated("TestRootNesting_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlatteningUsingFieldAutoComplete() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/nested_json/field_nesting_autocomplete")
                .addRelativeSource("TestFieldNestingAutocomplete.java")
                .addRelativeGenerated("TestFieldNestingAutocomplete_GsonTypeAdapter.java"))
    }
}
