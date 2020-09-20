package gsonpath

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import gsonpath.internal.GsonResultListTypeAdapterFactory
import java.lang.reflect.Type

/**
 * Adds serialization/deserialization handling for GsonPath specific types to Gson.
 *
 * This version of the class proxies to GsonPathTypeAdapterFactory, so it can be used as the sole factory.
 */
class GsonPathTypeAdapterFactoryKt : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        return gsonPathFactory.create(type, annotations, moshi)
                ?: gsonResultListFactory.create(type, annotations, moshi)
    }

    private val gsonPathFactory by lazy { GsonPathTypeAdapterFactory() }
    private val gsonResultListFactory by lazy { GsonResultListTypeAdapterFactory() }
}
