package gsonpath.generator.adapter.auto

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class SubstitutionTest : BaseGeneratorTest() {
    @Test
    fun testValidSubstitution() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/substitution/valid")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestSubstitutionBase.java")
                .addRelativeSource("TestSubstitutionImpl1.java")
                .addRelativeSource("TestSubstitutionImpl2.java")
                .addRelativeGenerated("TestSubstitutionImpl1_GsonTypeAdapter.java")
                .addRelativeGenerated("TestSubstitutionImpl2_GsonTypeAdapter.java"))
    }

    @Test
    fun testSubstitutionError_duplicateKeys() {
        val source = JavaFileObjects.forResource("adapter/auto/substitution/errors/TestSubstitutionError_DuplicateKeys.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("PathSubstitution original values must be unique")
                .`in`(source)
    }


}
