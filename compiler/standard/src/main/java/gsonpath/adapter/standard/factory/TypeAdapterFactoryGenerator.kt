package gsonpath.adapter.standard.factory

import com.squareup.javapoet.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.Constants.MOSHI
import gsonpath.adapter.Constants.NULL
import gsonpath.adapter.util.writeFile
import gsonpath.util.*
import java.lang.reflect.Type
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class TypeAdapterFactoryGenerator(private val fileWriter: FileWriter) {

    fun generate(factoryElement: TypeElement, generatedGsonAdapters: List<AdapterGenerationResult>) {
        val packageLocalHandleResults = TypeAdapterFactoryHandlersFactory
                .createResults(factoryElement, generatedGsonAdapters)

        for ((packageName, list) in packageLocalHandleResults) {
            createPackageLocalTypeAdapterLoaders(packageName, list)
        }

        createGsonTypeFactoryImpl(factoryElement, packageLocalHandleResults)
    }

    /**
     * Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
     */
    private fun createGsonTypeFactoryImpl(
            factoryElement: TypeElement,
            packageLocalAdapterGenerationResults: Map<String, List<AdapterGenerationResult>>) {

        val factoryClassName = ClassName.get(factoryElement)

        TypeSpecExt.finalClassBuilder(factoryClassName.simpleName() + "Impl")
                .addSuperinterface(factoryClassName)
                .gsonTypeFactoryImplContent(packageLocalAdapterGenerationResults)
                .writeFile(fileWriter, factoryClassName.packageName())
    }

    private fun TypeSpec.Builder.gsonTypeFactoryImplContent(
            packageLocalAdapterGenerationResults: Map<String, List<AdapterGenerationResult>>): TypeSpec.Builder {

        field(PACKAGE_PRIVATE_LOADERS, ArrayTypeName.of(JsonAdapter.Factory::class.java)) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        constructor {
            addModifiers(Modifier.PUBLIC)
            code {
                assignNew(PACKAGE_PRIVATE_LOADERS,
                        "\$T[${packageLocalAdapterGenerationResults.size}]",
                        JsonAdapter.Factory::class.java)

                // Add the package local type adapter loaders to the hash map.
                for ((index, packageName) in packageLocalAdapterGenerationResults.keys.withIndex()) {
                    assignNew("$PACKAGE_PRIVATE_LOADERS[$index]",
                            "$packageName.$PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME()")
                }
            }
        }

        //
        // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
        //
        overrideMethod("create") {
            returns(JsonAdapter::class.java)
            addParameter(Type::class.java, "type")
            addParameter(ParameterizedTypeName.get(ClassName.get(Set::class.java), WildcardTypeName.subtypeOf(Annotation::class.java)), "annotations")
            addParameter(Moshi::class.java, MOSHI)

            code {
                `for`("int i = 0; i < $PACKAGE_PRIVATE_LOADERS.length; i++") {
                    createVariable(JsonAdapter::class.java, TYPE_ADAPTER, "$PACKAGE_PRIVATE_LOADERS[i].create(type, annotations, $MOSHI)")
                    newLine()

                    `if`("$TYPE_ADAPTER != $NULL") {
                        `return`(TYPE_ADAPTER)
                    }
                }
                `return`(NULL)
            }
        }

        return this
    }

    private fun createPackageLocalTypeAdapterLoaders(
            packageName: String,
            packageLocalGsonAdapters: List<AdapterGenerationResult>) {

        TypeSpecExt.finalClassBuilder(ClassName.get(packageName, PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME))
                .addSuperinterface(JsonAdapter.Factory::class.java)
                .packagePrivateTypeAdapterLoaderContent(packageLocalGsonAdapters)
                .writeFile(fileWriter, packageName)
    }

    //
    // <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
    //
    private fun TypeSpec.Builder.packagePrivateTypeAdapterLoaderContent(
            packageLocalGsonAdapters: List<AdapterGenerationResult>): TypeSpec.Builder {

        overrideMethod("create") {
            returns(JsonAdapter::class.java)
            addParameter(Type::class.java, "type")
            addParameter(ParameterizedTypeName.get(ClassName.get(Set::class.java), WildcardTypeName.subtypeOf(Annotation::class.java)), "annotations")
            addParameter(Moshi::class.java, MOSHI)
            code {
                createVariable(Class::class.java, RAW_TYPE, "\$T.getRawType(type)", Types::class.java)

                for ((currentAdapterIndex, result) in packageLocalGsonAdapters.withIndex()) {
                    val condition = result.adapterGenericTypeClassNames
                            .joinToString(separator = " || ") { "rawType.equals(\$T.class)" }

                    if (currentAdapterIndex == 0) {
                        ifWithoutClose(condition, *result.adapterGenericTypeClassNames) {
                            `return`("new \$T($MOSHI)", result.adapterClassName)
                        }
                    } else {
                        newLine() // New line for easier readability.
                        elseIf(condition, *result.adapterGenericTypeClassNames) {
                            `return`("new \$T($MOSHI)", result.adapterClassName)
                        }
                    }
                }

                endControlFlow()
                newLine()
                `return`(NULL)
            }
        }
        return this
    }

    private companion object {
        private const val PACKAGE_PRIVATE_TYPE_ADAPTER_LOADER_CLASS_NAME = "PackagePrivateTypeAdapterLoader"
        private const val PACKAGE_PRIVATE_LOADERS = "mPackagePrivateLoaders"
        private const val TYPE_ADAPTER = "typeAdapter"
        private const val RAW_TYPE = "rawType"
    }
}
