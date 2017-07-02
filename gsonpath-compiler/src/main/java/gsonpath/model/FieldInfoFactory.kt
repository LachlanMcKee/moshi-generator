package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import gsonpath.ExcludeField

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class FieldInfoFactory(private val processingEnv: ProcessingEnvironment) {

    /**
     * Obtain all possible elements contained within the annotated class, including inherited fields.
     */
    fun getModelFieldsFromElement(modelElement: TypeElement, fieldsRequireAnnotation: Boolean): List<FieldInfo> {
        return processingEnv.elementUtils.getAllMembers(modelElement)
                .filter {
                    // Ignore modelElement that are not fields.
                    it.kind == ElementKind.FIELD
                }
                .filter {
                    // Ignore final, static and transient fields.
                    val modifiers = it.modifiers
                    !(modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT))
                }
                .filter {
                    !fieldsRequireAnnotation || it.getAnnotation(SerializedName::class.java) != null
                }
                .filter {
                    // Ignore any excluded fields
                    it.getAnnotation(ExcludeField::class.java) == null
                }
                .map { memberElement ->
                    // Ensure that any generics have been converted into their actual class.
                    val generifiedElement = processingEnv.typeUtils.asMemberOf(modelElement.asType() as DeclaredType, memberElement)

                    object : FieldInfo {
                        override val typeName: TypeName
                            get() = TypeName.get(generifiedElement)

                        override val typeMirror: TypeMirror
                            get() = generifiedElement

                        override val parentClassName: String
                            get() = memberElement.enclosingElement.toString()

                        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                            return memberElement.getAnnotation(annotationClass)
                        }

                        override val fieldName: String
                            get() = memberElement.simpleName.toString()

                        override val annotationNames: List<String>
                            get() {
                                return memberElement.annotationMirrors.map { it ->
                                    it.annotationType.asElement().simpleName.toString()
                                }
                            }

                        override val element: Element?
                            get() = memberElement

                        override val isDirectAccess: Boolean
                            get() = false
                    }
                }
    }

    fun getModelFieldsFromInterface(interfaceInfo: InterfaceInfo): List<FieldInfo> {
        return interfaceInfo.fieldInfo.map {
            object : FieldInfo {
                override val typeName: TypeName
                    get() = it.typeName

                override val typeMirror: TypeMirror
                    get() = it.typeMirror

                override val parentClassName: String
                    get() = interfaceInfo.parentClassName.toString()

                override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                    return it.elementInfo.getAnnotation(annotationClass)
                }

                override val fieldName: String
                    get() = it.fieldName

                override val annotationNames: List<String>
                    get() = it.elementInfo.annotationNames

                override val element: Element?
                    get() = it.elementInfo.underlyingElement

                override val isDirectAccess: Boolean
                    get() = it.isDirectAccess
            }
        }
    }

}
