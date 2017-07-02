package gsonpath.generator.standard

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class FieldTypesTest : BaseGeneratorTest() {
    @Test
    fun testValidPrimitives() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/primitives/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidPrimitives.java"),

                relativeGeneratedNames = listOf(
                        "TestValidPrimitives_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testInvalidPrimitives() {
        val source = JavaFileObjects.forResource("generator/standard/field_types/primitives/invalid/TestInvalidPrimitives.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path: Unsupported primitive type found. Only boolean, int, double and long can be used.")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testBoxedPrimitives() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/boxed_primitives",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestBoxedPrimitives.java"),

                relativeGeneratedNames = listOf(
                        "TestBoxedPrimitives_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testGenericFields() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/generics",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestGenerics.java"),

                relativeGeneratedNames = listOf(
                        "TestGenerics_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testCustomFieldType() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/custom_field",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestCustomField.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomField_GsonTypeAdapter.java")
        ))
    }

}
