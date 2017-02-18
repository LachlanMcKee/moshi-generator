package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class GenericsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/interfaces")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonPathModel.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseClass() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/classes")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseInterfaceAndClass() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/interfaces_and_classs")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }
}
