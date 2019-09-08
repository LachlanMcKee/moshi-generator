package gsonpath.adapter.standard.model

import gsonpath.model.Bah

interface BahFieldPathFetcher<in T : Bah> {
    fun getJsonFieldPath(bah: T, metadata: GsonObjectMetadata): FieldPath
}
