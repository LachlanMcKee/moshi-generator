package gsonpath.adapter

import com.squareup.javapoet.AnnotationSpec
import gsonpath.annotation.GsonPathGenerated

object Constants {
    const val MOSHI = "moshi"
    const val NULL = "null"
    const val READER = "reader"
    const val WRITER = "writer"
    const val VALUE = "value"

    val GENERATED_ANNOTATION: AnnotationSpec = AnnotationSpec.builder(GsonPathGenerated::class.java).build()
}
