package gsonpath.generator.adapter

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.generator.AdapterGeneratorDelegate
import gsonpath.generator.Generator
import gsonpath.model.InterfaceFieldInfo
import gsonpath.model.InterfaceInfo
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.MethodSpec
import gsonpath.internal.GsonPathElementList
import com.squareup.javapoet.TypeSpec

internal class ModelInterfaceGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {

    @Throws(ProcessingException::class)
    fun handle(element: TypeElement): InterfaceInfo {
        val interfaces = element.interfaces
        if (interfaces != null && interfaces.size == 1) {
            val typeName = TypeName.get(interfaces[0])

            if (typeName is ParameterizedTypeName) {
                if (typeName.rawType == TypeName.get(List::class.java)) {
                    return handleList(ClassName.get(element), typeName)
                }
            }
        }
        return handleStandard(element)
    }

    private fun createOutputClassName(modelClassName: ClassName): ClassName {
        val adapterGeneratorDelegate = AdapterGeneratorDelegate()
        return ClassName.get(modelClassName.packageName(),
                adapterGeneratorDelegate.generateClassName(modelClassName, "GsonPathModel"))
    }

    @Throws(ProcessingException::class)
    private fun handleList(modelClassName: ClassName, listTypeName: ParameterizedTypeName): InterfaceInfo {
        val outputClassName = createOutputClassName(modelClassName)

        val typeBuilder = TypeSpec.classBuilder(outputClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(GsonPathElementList::class.java), listTypeName.typeArguments[0]))
                .addSuperinterface(modelClassName)

        typeBuilder.addField(listTypeName, "internalList", Modifier.PRIVATE, Modifier.FINAL)

        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)

        constructorBuilder.addParameter(listTypeName, "internalList")
        constructorBuilder.addStatement("this.internalList = internalList")

        typeBuilder.addMethod(constructorBuilder.build())

        val getListMethod = MethodSpec.methodBuilder("getList")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PROTECTED)
                .returns(listTypeName)

        getListMethod.addStatement("return internalList")

        typeBuilder.addMethod(getListMethod.build())

        if (!writeFile(outputClassName.packageName(), typeBuilder)) {
            throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
        }

        val fieldInfo = arrayOf(InterfaceFieldInfo(object : InterfaceFieldInfo.ElementInfo {
            override val underlyingElement: Element?
                get() = null

            override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                return null
            }

            override val annotationNames: Array<String>
                get() = arrayOf("AutoGsonAdapter", "NonNull")

        }, listTypeName, "internalList", true))

        return InterfaceInfo(outputClassName, fieldInfo)
    }

    @Throws(ProcessingException::class)
    private fun handleStandard(element: TypeElement): InterfaceInfo {
        val modelClassName = ClassName.get(element)
        val outputClassName: ClassName = createOutputClassName(modelClassName)

        val typeBuilder = TypeSpec.classBuilder(outputClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(modelClassName)

        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)

        val methodElements = getMethodElements(element)

        // Equals method
        val equalsCodeBlock = CodeBlock.builder()
        equalsCodeBlock.addStatement("if (this == o) return true")
        equalsCodeBlock.addStatement("if (o == null || getClass() != o.getClass()) return false")
        equalsCodeBlock.add("\n")
        equalsCodeBlock.addStatement("\$T that = (\$T) o", outputClassName, outputClassName)
        equalsCodeBlock.add("\n")

        // Hash code method
        val hasCodeCodeBlock = CodeBlock.builder()

        // ToString method
        val toStringCodeBlock = CodeBlock.builder()
        toStringCodeBlock.add("return \"\$L{\" +\n", modelClassName.simpleName())

        // An optimisation for hash codes which prevents us creating too many temp long variables.
        val hasDoubleField = methodElements
                .map { it.asType() as ExecutableType }
                .map { it.returnType }
                .any { TypeName.get(it) == TypeName.DOUBLE }

        if (hasDoubleField) {
            hasCodeCodeBlock.addStatement("long temp")
        }

        val interfaceInfoList = ArrayList<InterfaceFieldInfo>()
        for (elementIndex in methodElements.indices) {
            val enclosedElement = methodElements[elementIndex]

            val methodType = enclosedElement.asType() as ExecutableType
            val returnType = methodType.returnType
            val typeName = TypeName.get(returnType)

            if (typeName == null || typeName == TypeName.VOID) {
                throw ProcessingException("Gson Path interface methods must have a return type", enclosedElement)
            }

            if (methodType.parameterTypes.isNotEmpty()) {
                throw ProcessingException("Gson Path interface methods must not have parameters", enclosedElement)
            }

            val methodName = enclosedElement.simpleName.toString()

            //
            // Transform the method name into the field name by removing the first camel-cased portion.
            // e.g. 'getName' becomes 'name'
            //
            val fieldName: String
            val indexOfFirst = methodName.indexOfFirst(Char::isUpperCase)
            if (indexOfFirst != -1) {
                fieldName = methodName[indexOfFirst].toLowerCase() + methodName.substring(indexOfFirst + 1)
            } else {
                fieldName = methodName
            }

            typeBuilder.addField(typeName, fieldName, Modifier.PRIVATE, Modifier.FINAL)

            val accessorMethod = MethodSpec.methodBuilder(methodName)
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)

            accessorMethod.addCode("return \$L;\n", fieldName)

            // Copy all annotations from the interface accessor method to the implementing classes accessor.
            val annotationMirrors = enclosedElement.annotationMirrors
            for (annotationMirror in annotationMirrors) {
                accessorMethod.addAnnotation(AnnotationSpec.get(annotationMirror))
            }

            typeBuilder.addMethod(accessorMethod.build())

            // Add the parameter to the constructor
            constructorBuilder.addParameter(typeName, fieldName)
            constructorBuilder.addStatement("this.\$L = \$L", fieldName, fieldName)

            interfaceInfoList.add(InterfaceFieldInfo(StandardElementInfo(enclosedElement), typeName, fieldName, false))

            // Add to the equals method
            if (typeName.isPrimitive) {
                equalsCodeBlock.addStatement("if (\$L != that.\$L) return false", fieldName, fieldName)
            } else {
                if (typeName is ArrayTypeName) {
                    equalsCodeBlock.addStatement("if (!java.util.Arrays.equals(\$L, that.\$L)) return false", fieldName, fieldName)

                } else {
                    equalsCodeBlock.addStatement("if (\$L != null ? !\$L.equals(that.\$L) : that.\$L != null) return false", fieldName, fieldName, fieldName, fieldName)
                }
            }

            // Add to the hash code method
            val hashCodeLine: String

            if (typeName.isPrimitive) {
                // The allowed primitive types are: int, long, double, boolean
                if (typeName == TypeName.INT) {
                    hashCodeLine = fieldName

                } else if (typeName == TypeName.LONG) {
                    hashCodeLine = String.format("(int) (%s ^ (%s >>> 32))", fieldName, fieldName)

                } else if (typeName == TypeName.DOUBLE) {
                    hasCodeCodeBlock.addStatement("temp = java.lang.Double.doubleToLongBits(\$L)", fieldName)
                    hashCodeLine = "(int) (temp ^ (temp >>> 32))"

                } else {
                    // Last possible outcome in a boolean.
                    hashCodeLine = String.format("(%s ? 1 : 0)", fieldName)
                }
            } else {
                if (typeName is ArrayTypeName) {
                    hashCodeLine = String.format("java.util.Arrays.hashCode(%s)", fieldName)

                } else {
                    hashCodeLine = String.format("%s != null ? %s.hashCode() : 0", fieldName, fieldName)
                }
            }

            if (elementIndex == 0) {
                hasCodeCodeBlock.addStatement("int result = \$L", hashCodeLine)
            } else {
                hasCodeCodeBlock.addStatement("result = 31 * result + (\$L)", hashCodeLine)
            }

            // Add to the toString method.
            toStringCodeBlock.add("\t\t\"")
            if (elementIndex > 0) {
                toStringCodeBlock.add(", ")
            }
            if (typeName is ArrayTypeName) {
                toStringCodeBlock.add("\$L=\" + java.util.Arrays.toString(\$L) +", fieldName, fieldName)

            } else {
                toStringCodeBlock.add("\$L=\" + \$L +", fieldName, fieldName)
            }
            toStringCodeBlock.add("\n", fieldName, fieldName)
        }

        typeBuilder.addMethod(constructorBuilder.build())

        // Add the equals method
        val equalsBuilder = MethodSpec.methodBuilder("equals")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addParameter(TypeName.OBJECT, "o")

        equalsCodeBlock.add("\n")
        equalsCodeBlock.addStatement("return true")
        equalsBuilder.addCode(equalsCodeBlock.build())
        typeBuilder.addMethod(equalsBuilder.build())

        // Add the hashCode method
        val hashCodeBuilder = MethodSpec.methodBuilder("hashCode")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)

        // If we have no elements, 'result' won't be initialised!
        if (methodElements.isNotEmpty()) {
            hasCodeCodeBlock.addStatement("return result")
        } else {
            hasCodeCodeBlock.addStatement("return 0")
        }

        hashCodeBuilder.addCode(hasCodeCodeBlock.build())
        typeBuilder.addMethod(hashCodeBuilder.build())

        // Add the hashCode method
        val toStringBuilder = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(String::class.java))

        toStringCodeBlock.add("\t\t'}';\n", modelClassName.simpleName())

        toStringBuilder.addCode(toStringCodeBlock.build())
        typeBuilder.addMethod(toStringBuilder.build())

        if (!writeFile(outputClassName.packageName(), typeBuilder)) {
            throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
        }

        return InterfaceInfo(outputClassName, interfaceInfoList.toTypedArray())
    }

    private fun getMethodElements(element: TypeElement): List<Element> {
        val methodElements = processingEnv.elementUtils.getAllMembers(element).filter {
            // Ignore methods from the base Object class
            it.kind == ElementKind.METHOD && TypeName.get(it.enclosingElement.asType()) != TypeName.OBJECT
        }
        return methodElements
    }

    private class StandardElementInfo constructor(override val underlyingElement: Element) : InterfaceFieldInfo.ElementInfo {

        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
            return underlyingElement.getAnnotation(annotationClass)
        }

        override val annotationNames: Array<String>
            get() {
                return underlyingElement.annotationMirrors.map { it ->
                    it.annotationType.asElement().simpleName.toString()
                }.toTypedArray()
            }
    }

}
