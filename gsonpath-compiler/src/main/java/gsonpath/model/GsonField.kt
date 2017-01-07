package gsonpath.model

import com.google.common.base.Objects

class GsonField(val fieldIndex: Int, val fieldInfo: FieldInfo, val jsonPath: String, val isRequired: Boolean) {

    val variableName: String
        get() = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val gsonField = o as GsonField?
        return fieldIndex == gsonField!!.fieldIndex &&
                isRequired == gsonField.isRequired &&
                Objects.equal(fieldInfo, gsonField.fieldInfo) &&
                Objects.equal(jsonPath, gsonField.jsonPath)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(fieldIndex, fieldInfo, jsonPath, isRequired)
    }

    override fun toString(): String {
        return "GsonField: {" +
                "fieldIndex=" + fieldIndex +
                ", fieldInfo=" + fieldInfo +
                ", jsonPath='" + jsonPath + '\'' +
                ", isRequired=" + isRequired +
                '}'
    }
}
