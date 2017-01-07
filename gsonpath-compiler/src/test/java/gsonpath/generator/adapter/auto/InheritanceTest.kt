package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class InheritanceTest : BaseGeneratorTest() {
    @Test
    fun testInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/inheritance")
                .addRelativeSource("TestInheritanceBase.java")
                .addRelativeSource("TestInheritance.java")
                .addRelativeGenerated("TestInheritance_GsonTypeAdapter.java"))
    }
}
