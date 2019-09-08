package gsonpath.adapter.standard.model

import gsonpath.model.Bah

interface GsonFieldValueFactory<T: Bah, R> {
    fun create(fieldInfo: T, variableName: String, jsonPath: String, required: Boolean): R
}
