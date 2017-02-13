package gsonpath.generator.adapter.auto

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class InheritanceTest : BaseGeneratorTest() {
    @Test
    fun testInheritance() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/auto/inheritance")
                .addAbsoluteSource("adapter/auto/TestGsonTypeFactory.java")
                .addRelativeSource("TestInheritanceBase.java")
                .addRelativeSource("TestInheritance.java")
                .addRelativeGenerated("TestInheritance_GsonTypeAdapter.java"))
    }
}
