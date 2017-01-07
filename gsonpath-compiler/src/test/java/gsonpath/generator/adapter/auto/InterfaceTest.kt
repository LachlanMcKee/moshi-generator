package gsonpath.generator.adapter.auto

import com.google.testing.compile.JavaFileObjects
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class InterfaceTest : BaseGeneratorTest() {
    @Test
    fun testValidInterface() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/interface_example/valid")
                .addRelativeSource("TestValidInterface.java")
                .addRelativeGenerated("TestValidInterface_GsonPathModel.java")
                .addRelativeGenerated("TestValidInterface_GsonTypeAdapter.java"))
    }

    @Test
    fun testInvalidInterface_returningVoid() {
        val source = JavaFileObjects.forResource("adapter/auto/interface_example/invalid/TestValidInterface_ReturningVoid.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must have a return type")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testInvalidInterface_withParameters() {
        val source = JavaFileObjects.forResource("adapter/auto/interface_example/invalid/TestValidInterface_WithParameters.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must not have parameters")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testUsingPrimitives() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/interface_example/primitive")
                .addRelativeSource("TestUsingPrimitives.java")
                .addRelativeGenerated("TestUsingPrimitives_GsonPathModel.java")
                .addRelativeGenerated("TestUsingPrimitives_GsonTypeAdapter.java"))
    }

    @Test
    fun testUsingInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/interface_example/inheritance")
                .addRelativeSource("TestUsingInheritanceBase.java")
                .addRelativeSource("TestUsingInheritance.java")
                .addRelativeGenerated("TestUsingInheritance_GsonPathModel.java")
                .addRelativeGenerated("TestUsingInheritance_GsonTypeAdapter.java"))
    }

    @Test
    fun testFlattenJsonWithInterface() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/interface_example/flatten_json")
                .addRelativeSource("TestFlattenJsonWithInterface.java")
                .addRelativeGenerated("TestFlattenJsonWithInterface_GsonPathModel.java")
                .addRelativeGenerated("TestFlattenJsonWithInterface_GsonTypeAdapter.java"))
    }
}
