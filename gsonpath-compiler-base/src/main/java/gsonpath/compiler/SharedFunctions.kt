package gsonpath.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

val GSON_SUPPORTED_CLASSES: Set<TypeName> = HashSet(Arrays.asList(
        TypeName.get(Boolean::class.java).box(),
        TypeName.get(Int::class.java).box(),
        TypeName.get(Long::class.java).box(),
        TypeName.get(Double::class.java).box(),
        TypeName.get(String::class.java).box()
))

val GSON_SUPPORTED_PRIMITIVE = HashSet(Arrays.asList(
        TypeName.BOOLEAN,
        TypeName.INT,
        TypeName.LONG,
        TypeName.DOUBLE
))
val CLASS_NAME_STRING: ClassName = ClassName.get(String::class.java)

fun createDefaultVariableValueForTypeName(typeName: TypeName): String {
    when (typeName) {
        TypeName.INT,
        TypeName.BYTE,
        TypeName.SHORT ->
            return "0"

        TypeName.LONG ->
            return "0L"

        TypeName.FLOAT ->
            return "0f"

        TypeName.DOUBLE ->
            return "0d"

        TypeName.CHAR ->
            return "'\\u0000'"

        TypeName.BOOLEAN ->
            return "false"

        else ->
            return "null"
    }
}

fun generateClassName(className: ClassName, classNameSuffix: String): String {
    //
    // We need to ensure that nested classes are have include their parent class as part of the name.
    // Otherwise this could cause file name contention when other nested classes have the same name
    //
    val fileName = className.simpleNames().joinToString("_")

    // Make sure no '.' managed to sneak through!
    return fileName.replace(".", "_") + "_" + classNameSuffix
}

fun isFieldCollectionType(processingEnv: ProcessingEnvironment, typeMirror: TypeMirror): Boolean {
    val rawType: TypeMirror = when (typeMirror) {
        is DeclaredType -> typeMirror.typeArguments.first()

        else -> return false
    }

    val collectionTypeElement = processingEnv.elementUtils.getTypeElement(Collection::class.java.name)
    val collectionType = processingEnv.typeUtils.getDeclaredType(collectionTypeElement, rawType)

    return processingEnv.typeUtils.isSubtype(typeMirror, collectionType)
}

fun CodeBlock.Builder.addWithNewLine(format: String, vararg args: Any): CodeBlock.Builder {
    this.add(format, *args)
    this.addNewLine()
    return this
}

fun CodeBlock.Builder.addNewLine(): CodeBlock.Builder {
    this.add("\n")
    return this
}

fun CodeBlock.Builder.addComment(comment: String): CodeBlock.Builder {
    this.add("// $comment\n")
    return this
}

fun CodeBlock.Builder.addEscapedStatement(format: String): CodeBlock.Builder {
    this.addStatement(format.replace("$", "$$"))
    return this
}