package gsonpath.util

import com.squareup.javapoet.*
import javax.lang.model.element.Modifier

object TypeSpecExt {
    fun finalClassBuilder(name: String): TypeSpec.Builder {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }

    fun finalClassBuilder(className: ClassName): TypeSpec.Builder {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }
}

fun TypeSpec.Builder.field(name: String, type: TypeName, func: FieldSpec.Builder.() -> Unit) {
    addField(FieldSpec.builder(type, name).apply(func).build())
}

fun TypeSpec.Builder.overrideMethod(name: String, func: MethodSpec.Builder.() -> Unit) {
    addMethod(MethodSpecExt.overrideMethodBuilder(name).applyAndBuild(func))
}

fun TypeSpec.Builder.constructor(func: MethodSpec.Builder.() -> Unit) {
    addMethod(MethodSpec.constructorBuilder().applyAndBuild(func))
}