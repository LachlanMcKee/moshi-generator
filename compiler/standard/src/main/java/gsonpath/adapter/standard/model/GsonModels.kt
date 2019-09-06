package gsonpath.adapter.standard.model

sealed class GsonModel<T>
sealed class GsonArrayElement<T> : GsonModel<T>()

data class GsonField<T>(val value: T) : GsonArrayElement<T>()

data class GsonObject<T>(private val fieldMap: Map<String, GsonModel<T>>) : GsonArrayElement<T>() {

    fun entries(): Set<Map.Entry<String, GsonModel<T>>> {
        return fieldMap.entries
    }

    fun size(): Int {
        return fieldMap.size
    }
}

data class GsonArray<T>(
        private val arrayFields: Map<Int, GsonArrayElement<T>> = HashMap(),
        val maxIndex: Int) : GsonModel<T>() {

    fun entries(): Set<Map.Entry<Int, GsonArrayElement<T>>> {
        return arrayFields.entries
    }

    operator fun get(arrayIndex: Int): GsonArrayElement<T>? {
        return arrayFields[arrayIndex]
    }
}