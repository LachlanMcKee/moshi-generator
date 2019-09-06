package gsonpath.adapter

import gsonpath.model.FieldInfo

class Foo(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean
)