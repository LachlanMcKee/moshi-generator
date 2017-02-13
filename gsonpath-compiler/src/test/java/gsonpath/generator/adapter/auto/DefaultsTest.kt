package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class DefaultsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/defaults")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestDefaultsConfig.java")
                .addRelativeSource("TestDefaultsUseInheritanceModel.java")
                .addRelativeGenerated("TestDefaultsUseInheritanceModel_GsonTypeAdapter.java"))
    }

    @Test
    fun testOverrideInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/defaults")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestDefaultsConfig.java")
                .addRelativeSource("TestDefaultsOverrideInheritanceModel.java")
                .addRelativeGenerated("TestDefaultsOverrideInheritanceModel_GsonTypeAdapter.java"))
    }
}
