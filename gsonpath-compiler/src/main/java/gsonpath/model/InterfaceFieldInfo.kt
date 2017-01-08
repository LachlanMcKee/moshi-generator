package gsonpath.model

import com.squareup.javapoet.TypeName

import javax.lang.model.element.Element

class InterfaceFieldInfo(val elementInfo: ElementInfo,
                         internal val typeName: TypeName,
                         val fieldName: String,
                         val isDirectAccess: Boolean) {

    interface ElementInfo {
        val underlyingElement: Element?

        fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

        val annotationNames: Array<String>
    }

}
