package gsonpath.generator.standard

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class InheritanceTest : BaseGeneratorTest() {
    @Test
    fun testInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/inheritance")
                .addAbsoluteSource("generator/standard/TestGsonTypeFactory.java")
                .addRelativeSource("TestInheritanceBase.java")
                .addRelativeSource("TestInheritance.java")
                .addRelativeGenerated("TestInheritance_GsonTypeAdapter.java"))
    }
}
