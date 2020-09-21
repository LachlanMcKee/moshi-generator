package gsonpath.adapter.standard.factory

import com.squareup.javapoet.TypeName
import com.squareup.moshi.JsonAdapter
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterGenerationResult
import javax.lang.model.element.TypeElement

object TypeAdapterFactoryHandlersFactory {
    private val typeAdapterFactoryTypeName = TypeName.get(JsonAdapter.Factory::class.java)

    fun createResults(
            factoryElement: TypeElement,
            generatedGsonAdapters: List<AdapterGenerationResult>): Map<String, List<AdapterGenerationResult>> {

        if (generatedGsonAdapters.isEmpty()) {
            return emptyMap()
        }

        // Ensure that the factory element only extends JsonAdapter.Factory
        val factoryInterfaces = factoryElement.interfaces
        if (factoryInterfaces.size != 1 || TypeName.get(factoryInterfaces[0]) != typeAdapterFactoryTypeName) {
            throw ProcessingException("Interfaces annotated with @AutoGsonAdapterFactory must extend " +
                    "com.squareup.moshi.JsonAdapter.Factory and no other interfaces.", factoryElement)
        }

        return generatedGsonAdapters.fold(emptyMap()) { map, generatedGsonAdapter ->
            val packageName = generatedGsonAdapter.adapterClassName.packageName()

            val newList: List<AdapterGenerationResult> =
                    map[packageName]?.plus(generatedGsonAdapter) ?: listOf(generatedGsonAdapter)

            return@fold map.plus(Pair(packageName, newList))
        }
    }
}
