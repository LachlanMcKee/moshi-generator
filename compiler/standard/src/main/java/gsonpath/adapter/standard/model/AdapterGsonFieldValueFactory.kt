package gsonpath.adapter.standard.model

import gsonpath.adapter.AdapterFieldMetadata
import gsonpath.model.AdapterFieldInfo

class AdapterGsonFieldValueFactory : GsonFieldValueFactory<AdapterFieldInfo, AdapterFieldMetadata> {
    override fun create(fieldInfo: AdapterFieldInfo, variableName: String, jsonPath: String, required: Boolean): AdapterFieldMetadata {
        return AdapterFieldMetadata(fieldInfo, variableName, jsonPath, required)
    }
}