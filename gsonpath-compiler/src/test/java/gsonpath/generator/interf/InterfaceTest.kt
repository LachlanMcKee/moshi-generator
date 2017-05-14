package gsonpath.generator.interf

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class InterfaceTest : BaseGeneratorTest() {
    @Test
    fun testValidInterface() {
        assertGeneratedContent(TestCriteria("generator/interf/valid")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestValidInterface.java")
                .addRelativeGenerated("TestValidInterface_GsonPathModel.java")
                .addRelativeGenerated("TestValidInterface_GsonTypeAdapter.java"))
    }

    @Test
    fun testInvalidInterface_returningVoid() {
        val source = JavaFileObjects.forResource("generator/interf/invalid/TestValidInterface_ReturningVoid.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must have a return type")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testInvalidInterface_withParameters() {
        val source = JavaFileObjects.forResource("generator/interf/invalid/TestValidInterface_WithParameters.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must not have parameters")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testUsingPrimitives() {
        assertGeneratedContent(TestCriteria("generator/interf/primitive")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestUsingPrimitives.java")
                .addRelativeGenerated("TestUsingPrimitives_GsonPathModel.java")
                .addRelativeGenerated("TestUsingPrimitives_GsonTypeAdapter.java"))
    }

    @Test
    fun testUsingInheritance() {
        assertGeneratedContent(TestCriteria("generator/interf/inheritance")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestUsingInheritanceBase.java")
                .addRelativeSource("TestUsingInheritance.java")
                .addRelativeGenerated("TestUsingInheritance_GsonPathModel.java")
                .addRelativeGenerated("TestUsingInheritance_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlattenJsonWithInterface() {
        assertGeneratedContent(TestCriteria("generator/interf/flatten_json")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestFlattenJsonWithInterface.java")
                .addRelativeGenerated("TestFlattenJsonWithInterface_GsonPathModel.java")
                .addRelativeGenerated("TestFlattenJsonWithInterface_GsonTypeAdapter.java"))
    }

    @Test
    fun testListInterface() {
        assertGeneratedContent(TestCriteria("generator/interf/list")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestListInterface.java")
                .addRelativeGenerated("TestListInterface_GsonPathModel.java")
                .addRelativeGenerated("TestListInterface_GsonTypeAdapter.java"))
    }
}
