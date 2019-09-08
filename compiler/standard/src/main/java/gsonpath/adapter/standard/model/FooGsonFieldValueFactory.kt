package gsonpath.adapter.standard.model

import gsonpath.adapter.Foo
import gsonpath.model.FieldInfo

class FooGsonFieldValueFactory : GsonFieldValueFactory<FieldInfo, Foo> {
    override fun create(fieldInfo: FieldInfo, variableName: String, jsonPath: String, required: Boolean): Foo {
        return Foo(fieldInfo, variableName, jsonPath, required)
    }
}