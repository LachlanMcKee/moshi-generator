package gsonpath.model

import com.google.common.base.Objects
import java.util.LinkedHashMap

sealed class GsonModel

data class GsonField(val fieldIndex: Int, val fieldInfo: FieldInfo, val jsonPath: String, val isRequired: Boolean) : GsonModel() {
    val variableName: String
        get() = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
}

class GsonObject : GsonModel() {
    private val fieldMap: LinkedHashMap<String, GsonModel> = LinkedHashMap()

    fun addObject(branchName: String, gsonObject: GsonObject): GsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addField(branchName: String, field: GsonField): GsonField {
        if (fieldMap.containsKey(branchName)) {
            throw IllegalArgumentException("Value already exists")
        }
        fieldMap[branchName] = field
        return field
    }

    fun entries(): Set<Map.Entry<String, GsonModel>> {
        return fieldMap.entries
    }

    fun size(): Int {
        return fieldMap.size
    }

    operator fun get(key: String): GsonModel? {
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