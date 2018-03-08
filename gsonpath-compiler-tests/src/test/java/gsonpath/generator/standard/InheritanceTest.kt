package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class InheritanceTest : BaseGeneratorTest() {
    @Test
    fun testInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/inheritance",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestInheritanceBase.java",
                        "TestInheritance.java"),

                relativeGeneratedNames = listOf(
                        "TestInheritance_GsonTypeAdapter.java")
        ))
    }
}
