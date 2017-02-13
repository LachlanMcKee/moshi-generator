package gsonpath.generator.adapter.factory

import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class TypeAdapterFactoryGeneratorTest : BaseGeneratorTest() {
    @Test
    fun testGeneratedLoader() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("adapter/loader")
                .addRelativeSource("TestGsonTypeFactory.java")
                .addRelativeSource("TestLoaderSource.java")
                .addRelativeSource("source2/TestLoaderSource.java")
                .addRelativeSource("source2/TestLoaderSource2.java")
                .addRelativeSource("source3/TestLoaderSource.java")
                .addRelativeGenerated("TestGsonTypeFactoryImpl.java")
                .addRelativeGenerated("source2/PackagePrivateTypeAdapterLoader.java")
                .addRelativeGenerated("source3/PackagePrivateTypeAdapterLoader.java"))
    }
}
