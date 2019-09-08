package gsonpath.adapter.standard.adapter.write

import com.squareup.javapoet.ClassName
import gsonpath.adapter.AdapterFieldMetadata
import gsonpath.adapter.standard.model.GsonObject

data class WriteParams(
        val elementClassName: ClassName,
        val rootElements: GsonObject<AdapterFieldMetadata>,
        val serializeNulls: Boolean
)