package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import gsonpath.ExcludeField

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import java.util.ArrayList

class FieldInfoFactory(private val processingEnv: ProcessingEnvironment) {

    fun getModelFieldsFromElement(modelElement: TypeElement, fieldsRequireAnnotation: Boolean): List<FieldInfo> {
        val fieldInfoList = ArrayList<FieldInfo>()

        // Obtain all possible elements contained within the annotated class, including inherited fields.
        for (memberElement in processingEnv.elementUtils.getAllMembers(modelElement)) {

            // Ignore modelElement that are not fields.
            if (memberElement.kind != ElementKind.FIELD) {
                continue
            }

            // Ignore final, static and transient fields.
            val modifiers = memberElement.modifiers
            if (modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT)) {
                continue
            }

            if (fieldsRequireAnnotation && memberElement.getAnnotation(SerializedName::class.java) == null) {
                continue
            }

            // Ignore any excluded fields
            if (memberElement.getAnnotation(ExcludeField::class.java) != null) {
                continue
            }

            fieldInfoList.add(object : FieldInfo {
                override val typeName: TypeName
                    get() = TypeName.get(memberElement.asType())

                override val parentClassName: String
                    get() = memberElement.enclosingElement.toString()

                override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                    return memberElement.getAnnotation(annotationClass)
                }

                override val fieldName: String
                    get() = memberElement.simpleName.toString()

                override val annotationNames: Array<String>
                    get() {
                        return memberElement.annotationMirrors.map { it ->
                            it.annotationType.asElement().simpleName.toString()
                        }.toTypedArray()
                    }

                override val element: Element
                    get() = memberElement
            })
        }
        return fieldInfoList
    }

    fun getModelFieldsFromInterface(interfaceInfo: InterfaceInfo): List<FieldInfo> {
        return interfaceInfo.fieldInfo.map {
            object : FieldInfo {
                override val typeName: TypeName
                    get() = it.typeName

                override val parentClassName: String
                    get() = interfaceInfo.parentClassName.toString()

                override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                    return it.methodElement.getAnnotation(annotationClass)
                }

                override val fieldName: String
                    get() = it.fieldName

                override val annotationNames: Array<String>
                    get() {
                        return it.methodElement.annotationMirrors.map { it ->
                            it.annotationType.asElement().simpleName.toString()
                        }.toTypedArray()
                    }

                override val element: Element
                    get() = it.methodElement
            }
        }
    }

}
