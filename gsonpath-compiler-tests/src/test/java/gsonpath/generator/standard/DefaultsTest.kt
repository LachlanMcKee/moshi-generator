package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class DefaultsTest : BaseGeneratorTest() {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/defaults",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestDefaultsConfig.java",
                        "TestDefaultsUseInheritanceModel.java"),

                relativeGeneratedNames = listOf(
                        "TestDefaultsUseInheritanceModel_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testOverrideInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/defaults",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestDefaultsConfig.java",
                        "TestDefaultsOverrideInheritanceModel.java"),

                relativeGeneratedNames = listOf(
                        "TestDefaultsOverrideInheritanceModel_GsonTypeAdapter.java")
        ))
    }
}
