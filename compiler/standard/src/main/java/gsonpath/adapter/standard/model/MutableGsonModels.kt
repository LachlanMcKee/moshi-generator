package gsonpath.adapter.standard.model

import gsonpath.ProcessingException
import java.util.*

sealed class MutableGsonModel<T>
sealed class MutableGsonArrayElement<T> : MutableGsonModel<T>()

data class MutableGsonField<T>(val value: T) : MutableGsonArrayElement<T>() {
    fun toImmutable(): GsonField<T> {
        return GsonField(value)
    }
}

data class MutableGsonObject<T>(
        private val fieldMap: LinkedHashMap<String, MutableGsonModel<T>> = LinkedHashMap()) : MutableGsonArrayElement<T>() {

    fun addObject(branchName: String, gsonObject: MutableGsonObject<T>): MutableGsonObject<T> {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addArray(branchName: String): MutableGsonArray<T> {
        val array = fieldMap[branchName] as MutableGsonArray?
        if (array != null) {
            return array
        }

        val newArray = MutableGsonArray<T>()
        fieldMap[branchName] = newArray
        return newArray
    }

    @Throws(IllegalArgumentException::class)
    fun addField(branchName: String, field: MutableGsonField<T>): MutableGsonField<T> {
        if (fieldMap.containsKey(branchName)) {
            throw IllegalArgumentException("Value already exists")
        }
        fieldMap[branchName] = field
        return field
    }

    operator fun get(key: String): MutableGsonModel<T>? {
        return fieldMap[key]
    }

    fun toImmutable(): GsonObject<T> {
        return GsonObject(fieldMap.entries
                .asSequence()
                .map { (key, value) ->
                    when (value) {
                        is MutableGsonField -> key to value.toImmutable()
                        is MutableGsonObject -> key to value.toImmutable()
                        is MutableGsonArray -> key to value.toImmutable()
                    }
                }
                .toMap())
    }
}

data class MutableGsonArray<T>(
        private val arrayFields: MutableMap<Int, MutableGsonArrayElement<T>> = HashMap()) : MutableGsonModel<T>() {

    @Throws(IllegalArgumentException::class)
    fun addField(arrayIndex: Int, field: MutableGsonField<T>) {
        if (arrayFields.containsKey(arrayIndex)) {
            throw IllegalArgumentException("Value already exists")
        }
        arrayFields[arrayIndex] = field
    }

    @Throws(IllegalArgumentException::class)
    fun getObjectAtIndex(arrayIndex: Int): MutableGsonObject<T> {
        val gsonObject = arrayFields[arrayIndex] as MutableGsonObject?
        if (gsonObject != null) {
            return gsonObject
        }

        val newGsonObject = MutableGsonObject<T>()
        arrayFields[arrayIndex] = newGsonObject
        return newGsonObject
    }

    operator fun get(arrayIndex: Int): Any? {
        return arrayFields[arrayIndex]
    }

    fun toImmutable(): GsonArray<T> {
        return arrayFields.entries.let { entries ->
            val maxIndex = entries.maxBy { it.key }?.key
                    ?: throw ProcessingException("Array should not be empty")

            GsonArray(
                    maxIndex = maxIndex,
                    arrayFields = entries
                            .asSequence()
                            .map { (key, value) ->
                                when (value) {
                                    is MutableGsonField -> key to value.toImmutable()
                                    is MutableGsonObject -> key to value.toImmutable()
                                }
                            }
                            .toMap())
        }
    }
}