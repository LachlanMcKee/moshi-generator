package gsonpath.integration.factory

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessor
import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class TypeAdapterFactoryGeneratorTest {
    @Test
    fun givenTypeAdaptersAndTypeFactory_whenProcessorRuns_expectFactoryImplAndLoaders() {
        assertGeneratedContent(TestCriteria("generator/factory",

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
        assertAbout(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("generator/factory/NonGsonPathFile.java"))
                .processedWith(GsonProcessor())
                .compilesWithoutError()
    }

    @Test
    fun givenTypeAdaptersAndNoTypeAdapterFactory_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(emptyList(), "Gson Path: An interface annotated with @AutoGsonAdapterFactory (that directly " +
                "extends JsonAdapter.Factory) must exist before the annotation processor can succeed. " +
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
                "Gson Path: Interfaces annotated with @AutoGsonAdapterFactory must extend com.squareup.moshi.JsonAdapter.Factory and no other interfaces.")
    }

    @Test
    fun givenTypeAdaptersAndTypeAdapterNotExtendingAnyInterfaces_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactoryNoInterfaces.java"),
                "Gson Path: Interfaces annotated with @AutoGsonAdapterFactory must extend com.squareup.moshi.JsonAdapter.Factory and no other interfaces.")
    }

    @Test
    fun givenTypeAdaptersAndTypeAdapterNotNotAnInterfaces_whenProcessorRuns_expectError() {
        assertTypeAdapterFactoryFailure(listOf("TestGsonTypeFactoryAsClass.java"),
                "Gson Path: interface gsonpath.annotation.AutoGsonAdapterFactory can only be used with types: [INTERFACE]")
    }

    private fun assertTypeAdapterFactoryFailure(typeAdapterFactoryFileName: List<String>, errorMessage: String) {
        assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(JavaFileObjects.forResource("generator/factory/TestLoaderSource.java"))
                        .plus(typeAdapterFactoryFileName.map { JavaFileObjects.forResource("generator/factory/$it") })
                )
                .processedWith(GsonProcessor())
                .failsToCompile()
                .withErrorContaining(errorMessage)
    }
}
