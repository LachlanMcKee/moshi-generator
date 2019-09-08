package gsonpath.adapter

import gsonpath.model.AdapterFieldInfo

data class AdapterFieldMetadata(
        val fieldInfo: AdapterFieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean
)