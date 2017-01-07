package gsonpath.generator

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec

import java.io.IOException

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

abstract class Generator protected constructor(protected val processingEnv: ProcessingEnvironment) {

    protected fun writeFile(packageName: String, typeBuilder: TypeSpec.Builder): Boolean {
        try {
            val builder = JavaFile.builder(packageName, typeBuilder.build())
            onJavaFileBuilt(builder)
            builder.build().writeTo(processingEnv.filer)
            return true

        } catch (e: IOException) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Error while writing javapoet file: " + e.message)

            return false
        }

    }

    protected open fun onJavaFileBuilt(builder: JavaFile.Builder) {
        // Do nothing.
    }
}
