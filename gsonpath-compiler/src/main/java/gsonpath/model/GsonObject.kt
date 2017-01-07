package gsonpath.model

import com.google.common.base.Objects

import java.util.LinkedHashMap

class GsonObject {
    private val fieldMap: LinkedHashMap<String, Any>

    init {
        fieldMap = LinkedHashMap<String, Any>()
    }

    fun addObject(branchName: String, gsonObject: GsonObject): GsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addField(branchName: String, field: GsonField): GsonField {
        if (containsKey(branchName)) {
            throw IllegalArgumentException("Value already exists")
        }
        fieldMap[branchName] = field
        return field
    }

    fun size(): Int {
        return fieldMap.size
    }

    fun keySet(): Set<String> {
        return fieldMap.keys
    }

    operator fun get(key: String): Any? {
        return fieldMap[key]
    }

    fun containsKey(key: String): Boolean {
        return fieldMap.containsKey(key)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val gsonObject = other as GsonObject?
        return Objects.equal(fieldMap, gsonObject!!.fieldMap)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(fieldMap)
    }

    override fun toString(): String {
        return "GsonObject: " + fieldMap
    }
}
