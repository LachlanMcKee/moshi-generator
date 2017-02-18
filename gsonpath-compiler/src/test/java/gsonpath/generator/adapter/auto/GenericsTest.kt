package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class GenericsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/interfaces")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonPathModel.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseClass() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/classes")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }

    @Test
    fun testUseInterfaceAndClass() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/generics/interfaces_and_classs")
                .addRelativeSource("BaseTest.java")
                .addRelativeSource("IntermediateTest.java")
                .addRelativeSource("GenericsTest.java")
                .addRelativeGenerated("GenericsTest_GsonTypeAdapter.java"))
    }
}
