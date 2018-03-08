package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import com.sun.source.tree.*
import com.sun.source.util.TreePathScanner
import com.sun.source.util.Trees
import com.sun.tools.javac.tree.JCTree
import gsonpath.ExcludeField
import gsonpath.ProcessingException

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

class FieldInfoFactory(private val processingEnv: ProcessingEnvironment) {

    /**
     * Obtain all possible elements contained within the annotated class, including inherited fields.
     */
    fun getModelFieldsFromElement(modelElement: TypeElement, fieldsRequireAnnotation: Boolean, useConstructor: Boolean): List<FieldInfo> {
        val allMembers = processingEnv.elementUtils.getAllMembers(modelElement)

        return allMembers
                .filter {
                    // Ignore modelElement that are not fields.
                    it.kind == ElementKind.FIELD
                }
                .filter {
                    // Ignore static and transient fields.
                    val modifiers = it.modifiers
                    !(modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT))
                }
                .filter {
                    // If a field is final, we only add it if we are using a constructor to assign it.
                    !it.modifiers.contains(Modifier.FINAL) || useConstructor
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

                        override val fieldAccessor: String
                            get() {
                                return if (!memberElement.modifiers.contains(Modifier.PRIVATE)) {
                                    memberElement.simpleName.toString()
                                } else {
                                    findFieldGetterMethodName(allMembers, memberElement) + "()"
                                }
                            }

                        override val annotationNames: List<String>
                            get() {
                                return memberElement.annotationMirrors.map { it ->
                                    it.annotationType.asElement().simpleName.toString()
                                }
                            }

                        override val element: Element
                            get() = memberElement

                        override val hasDefaultValue: Boolean
                            get() {
                                return DefaultValueScanner().scan(
                                        Trees.instance(processingEnv).getPath(memberElement),
                                        null) != null
                            }
                    }
                }
    }

    /**
     * Attempts to find a logical getter method for a variable.
     *
     * For example, the following getter method names are valid for a variable named 'foo':
     * 'foo()', 'isFoo()', 'hasFoo()', 'getFoo()'
     *
     * If no getter method is found, an exception will be fired.
     *
     * @param allMembers all elements within the class.
     * @param variableElement the field element we want to find the getter method for.
     */
    private fun findFieldGetterMethodName(allMembers: List<Element>, variableElement: Element): String {
        val method = allMembers
                .filter { it.kind == ElementKind.METHOD }
                .filter {
                    // See if the method name either matches the variable name, or starts with a standard getter prefix.
                    val remainder = it.simpleName.toString().toLowerCase().replace(variableElement.simpleName.toString().toLowerCase(), "")
                    arrayOf("", "is", "has", "get").contains(remainder)
                }
                .find { (it.asType() as ExecutableType).parameterTypes.size == 0 }
                ?: throw ProcessingException("Unable to find getter for private variable", variableElement)

        return method.simpleName.toString()
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

                override val fieldAccessor: String
                    get() = it.getterMethodName + "()"

                override val annotationNames: List<String>
                    get() = it.elementInfo.annotationNames

                override val element: Element
                    get() = it.elementInfo.underlyingElement

                override val hasDefaultValue: Boolean
                    get() = false
            }
        }
    }

    /**
     * Scans a field and detects whether a default value has been set.
     *
     * If a value has been set, the result will be an empty list, otherwise it will be null.
     */
    private class DefaultValueScanner : TreePathScanner<List<String>?, Void>() {
        override fun visitVariable(node: VariableTree?, p: Void?): List<String>? {
            if ((node as JCTree.JCVariableDecl).init != null) {
                return emptyList()
            }
            return null
        }
    }

}
