package gsonpath.generator.adapter

import com.google.gson.JsonElement
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import java.util.*

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
val CLASS_NAME_JSON_ELEMENT: ClassName = ClassName.get(JsonElement::class.java)
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
    var fileName = ""
    for (name in className.simpleNames()) {
        fileName += name + "_"
    }

    // Make sure no '.' managed to sneak through!
    return fileName.replace(".", "_") + classNameSuffix
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