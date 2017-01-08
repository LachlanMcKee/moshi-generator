package gsonpath.model

import com.squareup.javapoet.TypeName

import javax.lang.model.element.Element

interface FieldInfo {
    val typeName: TypeName

    val parentClassName: String

    fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

    val fieldName: String

    val annotationNames: Array<String>

    val element: Element?

    val isDirectAccess: Boolean
}
