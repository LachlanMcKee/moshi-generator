package gsonpath.generator.adapter.auto

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class FieldTypesTest : BaseGeneratorTest() {
    @Test
    fun testValidPrimitives() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_types/primitives/valid")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestValidPrimitives.java")
                .addRelativeGenerated("TestValidPrimitives_GsonTypeAdapter.java"))
    }

    @Test
    fun testInvalidPrimitives() {
        val source = JavaFileObjects.forResource("adapter/auto/field_types/primitives/invalid/TestInvalidPrimitives.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path: Unsupported primitive type found. Only boolean, int, double and long can be used.")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testBoxedPrimitives() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_types/boxed_primitives")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestBoxedPrimitives.java")
                .addRelativeGenerated("TestBoxedPrimitives_GsonTypeAdapter.java"))
    }

    @Test
    fun testGenericFields() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_types/generics")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestGenerics.java")
                .addRelativeGenerated("TestGenerics_GsonTypeAdapter.java"))
    }

    @Test
    fun testCustomFieldType() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/field_types/custom_field")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestCustomField.java")
                .addRelativeGenerated("TestCustomField_GsonTypeAdapter.java"))
    }

}
