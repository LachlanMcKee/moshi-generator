package gsonpath.adapter.standard.model

import gsonpath.model.FieldInfo

interface FieldInfoPathFetcher<in T : FieldInfo> {
    fun getJsonFieldPath(fieldInfo: T, metadata: GsonObjectMetadata): FieldPath
}
