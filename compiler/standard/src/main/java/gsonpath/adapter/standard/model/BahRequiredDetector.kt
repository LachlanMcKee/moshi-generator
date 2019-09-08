package gsonpath.adapter.standard.model

import gsonpath.model.Bah

interface BahRequiredDetector<in T : Bah> {
    fun isRequired(bah: T, metadata: GsonObjectMetadata): Boolean
}

