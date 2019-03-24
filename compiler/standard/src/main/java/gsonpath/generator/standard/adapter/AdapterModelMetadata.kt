package gsonpath.generator.standard.adapter

import com.squareup.javapoet.ClassName
import gsonpath.generator.standard.adapter.read.ReadParams
import gsonpath.generator.standard.adapter.write.WriteParams
import gsonpath.model.GsonObject
import gsonpath.model.MandatoryFieldInfoFactory

data class AdapterModelMetadata(
        val modelClassName: ClassName,
        val adapterGenericTypeClassNames: List<ClassName>,
        val adapterClassName: ClassName,
        val isModelInterface: Boolean,
        val rootGsonObject: GsonObject,
        val mandatoryInfoMap: Map<String, MandatoryFieldInfoFactory.MandatoryFieldInfo>,
        val readParams: ReadParams,
        val writeParams: WriteParams
)