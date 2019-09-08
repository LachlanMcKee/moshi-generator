package gsonpath.adapter.standard.model

import gsonpath.model.FieldInfo

interface FieldInfoRequiredDetector<in T : FieldInfo> {
    fun isRequired(fieldInfo: T, metadata: GsonObjectMetadata): Boolean
}

