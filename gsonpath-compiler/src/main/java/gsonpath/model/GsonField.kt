package gsonpath.model

data class GsonField(val fieldIndex: Int, val fieldInfo: FieldInfo, val jsonPath: String, val isRequired: Boolean) {
    val variableName: String
        get() = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
}
