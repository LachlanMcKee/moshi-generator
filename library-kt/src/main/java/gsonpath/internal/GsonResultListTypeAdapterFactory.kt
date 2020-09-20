package gsonpath.internal

import com.squareup.moshi.*
import gsonpath.GsonResult
import gsonpath.GsonResultList
import gsonpath.internal.GsonUtil.isValidValue
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A factory for the list that stores valid and invalid results when parsing via Gson.
 */
internal class GsonResultListTypeAdapterFactory : JsonAdapter.Factory {

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (GsonResultList::class.java != Types.getRawType(type)) {
            return null
        }

        val elementType = (type as ParameterizedType).actualTypeArguments[0]
        return Adapter(moshi.adapter<Any>(elementType))
    }

    private class Adapter<E>(private val elementTypeAdapter: JsonAdapter<E>) : JsonAdapter<GsonResultList<E>>() {
        override fun fromJson(reader: JsonReader): GsonResultList<E>? {
            if (!isValidValue(reader)) {
                return null
            }

            return GsonResultList((reader.readJsonValue() as List<*>)
                    .map { jsonValue ->
                        try {
                            GsonResult.Success(elementTypeAdapter.fromJsonValue(jsonValue)!!)
                        } catch (e: Exception) {
                            GsonResult.Failure<E>(e)
                        }
                    })
        }

        override fun toJson(writer: JsonWriter, value: GsonResultList<E>?) {
            throw IllegalStateException("Writing is not supported by GsonResultList")
        }
    }
}
