package gsonpath.generator.streamer

import com.squareup.javapoet.*
import gsonpath.GsonArrayStreamer
import gsonpath.generator.Generator
import gsonpath.generator.HandleResult
import gsonpath.internal.GsonArrayStreamerLoader

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

class StreamArrayLoaderGenerator(processingEnv: ProcessingEnvironment) : Generator(processingEnv) {

    fun generate(generatedGsonArrayAdapters: List<HandleResult>): Boolean {
        if (generatedGsonArrayAdapters.isEmpty()) {
            return false
        }

        // Create the GsonPathLoader which is used by the GsonPathTypeAdapterFactory class.
        val typeBuilder = TypeSpec.classBuilder("GeneratedGsonArrayStreamerLoader")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(GsonArrayStreamerLoader::class.java)

        //
        // <T extends GsonArrayStreamer> T get(Class<T> type);
        //
        val typeVariableName = TypeVariableName.get("T", GsonArrayStreamer::class.java)
        val getMethod = MethodSpec.methodBuilder("get")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(typeVariableName)
                .returns(typeVariableName)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Class::class.java), typeVariableName), "type")

        val codeBlock = CodeBlock.builder()

        for ((currentAdapterIndex, result) in generatedGsonArrayAdapters.withIndex()) {
            if (currentAdapterIndex == 0) {
                codeBlock.beginControlFlow("if (type.equals(\$L.class))", result.originalClassName.toString())
            } else {
                codeBlock.add("\n") // New line for easier readability.
                codeBlock.nextControlFlow("else if (type.equals(\$L.class))", result.originalClassName.toString())
            }
            codeBlock.addStatement("return (T) new \$L()", result.generatedClassName.toString())

        }
        codeBlock.endControlFlow()
        codeBlock.add("\n")
        codeBlock.addStatement("return null")

        getMethod.addCode(codeBlock.build())
        typeBuilder.addMethod(getMethod.build())

        return writeFile("gsonpath", typeBuilder)
    }

}
