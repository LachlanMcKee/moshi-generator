package gsonpath.generator.adapter.loader

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class TypeAdapterLoaderGeneratorTest : BaseGeneratorTest() {
    @Test
    fun testGeneratedLoader() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/loader")
                .addRelativeSource("TestLoaderSource.java")
                .addRelativeSource("source2/TestLoaderSource.java")
                .addRelativeSource("source2/TestLoaderSource2.java")
                .addRelativeSource("source3/TestLoaderSource.java")
                .addRelativeGenerated("GeneratedTypeAdapterLoader.java")
                .addRelativeGenerated("PackagePrivateTypeAdapterLoader.java")
                .addRelativeGenerated("source2/PackagePrivateTypeAdapterLoader.java")
                .addRelativeGenerated("source3/PackagePrivateTypeAdapterLoader.java"))
    }
}
