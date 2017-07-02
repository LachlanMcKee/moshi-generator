package gsonpath.generator.factory

import com.google.common.truth.Truth.*
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessorImpl
import gsonpath.generator.BaseGeneratorTest
import org.junit.Test

class TypeAdapterFactoryGeneratorTest : BaseGeneratorTest() {
    @Test
    fun givenTypeAdaptersAndTypeFactory_whenProcessorRuns_expectFactoryImplAndLoaders() {
        assertGeneratedContent(BaseGeneratorTest.TestCriteria("generator/factory",

                relativeSourceNames = listOf(
                        "TestGsonTypeFactory.java",
                        "TestLoaderSource.java",
                        "source2/TestLoaderSource.java",
                        "source2/TestLoaderSource2.java",
                        "source3/TestLoaderSource.java"),

                relativeGeneratedNames = listOf(
                        "TestGsonTypeFactoryImpl.java",
                        "source2/PackagePrivateTypeAdapterLoader.java",
                        "source3/PackagePrivateTypeAdapterLoader.java")
        ))
    }

    @Test
    fun givenNoTypeAdaptersAndNoTypeAdapterFactory_whenProcessorRuns_expectNoErrors() {
        val criteria = BaseGeneratorTest.TestCriteria("generator/factory",
                relativeSourceNames = listOf("NonGsonPathFile.java"))

        assertAbout(JavaSourceSubjectFactory.javaSource()).that(criteria.getSourceFileObject(0))
                .processedWith(GsonProcessorImpl())
                .compilesWithoutError()
    }

    @Test
    fun givenTypeAdaptersAndNoTypeAdapterFactory_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(emptyList(), "Gson Path: An interface annotated with @AutoGsonAdapterFactory (that directly " +
                "extends com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                "See the AutoGsonAdapterFactory annotation for further details.")
    }

    @Test
    fun givenTypeAdaptersAndTooManyTypeAdapterFactories_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactory.java", "TestGsonTypeFactory2.java"),
                "Gson Path: Only one interface annotated with @AutoGsonAdapterFactory can exist")
    }

    @Test
    fun givenTypeAdaptersAndTypeAdapterNotExtendingTypeAdapterFactory_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactoryIncorrectInterfaces.java"),
                "Gson Path: Interfaces annotated with @AutoGsonAdapterFactory must extend com.google.gson.TypeAdapterFactory and no other interfaces.")
    }

    @Test
    fun givenTypeAdaptersAndTypeAdapterNotExtendingAnyInterfaces_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactoryNoInterfaces.java"),
                "Gson Path: Interfaces annotated with @AutoGsonAdapterFactory must extend com.google.gson.TypeAdapterFactory and no other interfaces.")
    }

    @Test
    fun givenTypeAdaptersAndTypeAdapterNotNotAnInterfaces_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactoryAsClass.java"),
                "Gson Path: Types annotated with @AutoGsonAdapterFactory must be an interface that directly extends com.google.gson.TypeAdapterFactory.")
    }

    private fun assertTypeAdapterFactoryFailure(typeAdapterFactoryFileName: List<String>, errorMessage: String) {
        val criteria = BaseGeneratorTest.TestCriteria("generator/factory",
                relativeSourceNames = listOf("TestLoaderSource.java").plus(typeAdapterFactoryFileName)
        )

        val sourceFilesSize = criteria.sourceFilesSize
        val sources = (0..sourceFilesSize - 1).map { criteria.getSourceFileObject(it) }

        assertAbout(JavaSourcesSubjectFactory.javaSources()).that(sources)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining(errorMessage)
    }
}
