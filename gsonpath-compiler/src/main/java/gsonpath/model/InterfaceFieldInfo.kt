package gsonpath.model

import com.squareup.javapoet.TypeName

import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

class InterfaceFieldInfo(val elementInfo: ElementInfo,
                         internal val typeName: TypeName,
                         internal val typeMirror: TypeMirror,
                         val fieldName: String,
                         val isDirectAccess: Boolean) {

    interface ElementInfo {
        val underlyingElement: Element?

        fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

        val annotationNames: List<String>
    }

}
