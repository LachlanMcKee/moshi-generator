package gsonpath.adapter

import gsonpath.model.FieldInfo

data class Foo(
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean
)