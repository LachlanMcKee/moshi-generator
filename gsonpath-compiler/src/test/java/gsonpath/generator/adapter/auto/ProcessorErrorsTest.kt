package gsonpath.generator.adapter.auto

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class ProcessorErrorsTest : BaseGeneratorTest() {
    @Test
    fun testInvalidFieldType() {
        val source = JavaFileObjects.forResource("adapter/auto/processor_errors/TestInvalidFieldTypeError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Invalid field type: java.lang.Object")
                .`in`(source)
                .onLine(8)
    }

    @Test
    fun testInvalidFieldPath() {
        val source = JavaFileObjects.forResource("adapter/auto/processor_errors/TestInvalidFieldPathError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Unexpected duplicate field 'value' found. Each tree branch must use a unique value!")
                .`in`(source)
                .onLine(16)
    }

    @Test
    fun testDuplicateFieldError() {
        val source = JavaFileObjects.forResource("adapter/auto/processor_errors/TestDuplicateFieldError.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Unexpected duplicate field 'value' found. Each tree branch must use a unique value!")
                .`in`(source)
                .onLine(13)
    }
}
