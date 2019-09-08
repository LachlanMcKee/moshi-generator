package gsonpath.adapter.standard.model

import gsonpath.model.FieldInfo

interface GsonFieldValueFactory<T: FieldInfo, R> {
    fun create(fieldInfo: T, variableName: String, jsonPath: String, required: Boolean): R
}
