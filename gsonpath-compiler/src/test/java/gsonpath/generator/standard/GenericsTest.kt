package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class GenericsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/interfaces")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonPathModel.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseClass() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/classes")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseInterfaceAndClass() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/interfaces_and_classs")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }
}
