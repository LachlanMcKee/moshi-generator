package gsonpath.model

import com.google.common.base.Objects
import java.util.HashMap
import java.util.LinkedHashMap

sealed class GsonModel
sealed class GsonArrayElement : GsonModel()

data class GsonField(val fieldIndex: Int, val fieldInfo: FieldInfo, val jsonPath: String, val isRequired: Boolean) : GsonArrayElement() {
    val variableName: String
        get() = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
}

class GsonObject : GsonArrayElement() {
    private val fieldMap: LinkedHashMap<String, GsonModel> = LinkedHashMap()

    fun addObject(branchName: String, gsonObject: GsonObject): GsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addArray(branchName: String): GsonArray {
        val array = fieldMap[branchName] as GsonArray?
        if (array != null) {
            return array
        }

        val newArray = GsonArray()
        fieldMap.put(branchName, newArray)
        return newArray
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

class GsonArray : GsonModel() {
    private val arrayFields: MutableMap<Int, GsonArrayElement>

    init {
        this.arrayFields = HashMap<Int, GsonArrayElement>()
    }

    @Throws(IllegalArgumentException::class)
    fun addField(arrayIndex: Int, field: GsonField) {
        if (containsKey(arrayIndex)) {
            throw IllegalArgumentException("Value already exists")
        }
        arrayFields.put(arrayIndex, field)
    }

    @Throws(IllegalArgumentException::class)
    fun getObjectAtIndex(arrayIndex: Int): GsonObject {
        val gsonObject = arrayFields[arrayIndex] as GsonObject?
        if (gsonObject != null) {
            return gsonObject
        }

        val newGsonObject = GsonObject()
        arrayFields.put(arrayIndex, newGsonObject)
        return newGsonObject
    }

    fun entries(): Set<Map.Entry<Int, GsonArrayElement>> {
        return arrayFields.entries
    }

    operator fun get(arrayIndex: Int): Any? {
        return arrayFields[arrayIndex]
    }

    fun containsKey(arrayIndex: Int?): Boolean {
        return arrayFields.containsKey(arrayIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || javaClass != other.javaClass) return false
        val gsonArray = other as GsonArray?

        return Objects.equal(arrayFields, gsonArray!!.arrayFields)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(arrayFields)
    }

    override fun toString(): String {
        return "GsonArray: " + arrayFields
    }
}