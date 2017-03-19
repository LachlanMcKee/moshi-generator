package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class DefaultsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/defaults")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestDefaultsConfig.java")
                .addRelativeSource("TestDefaultsUseInheritanceModel.java")
                .addRelativeGenerated("TestDefaultsUseInheritanceModel_GsonTypeAdapter.java"))
    }

    @Test
    fun testOverrideInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/defaults")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestDefaultsConfig.java")
                .addRelativeSource("TestDefaultsOverrideInheritanceModel.java")
                .addRelativeGenerated("TestDefaultsOverrideInheritanceModel_GsonTypeAdapter.java"))
    }
}
