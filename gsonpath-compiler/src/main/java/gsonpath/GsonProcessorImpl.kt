package gsonpath

import gsonpath.generator.HandleResult
import gsonpath.generator.adapter.standard.AutoGsonAdapterGenerator
import gsonpath.generator.adapter.loader.TypeAdapterLoaderGenerator
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

open class GsonProcessorImpl : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null || annotations.isEmpty()) {
            return false
        }

        println()
        printMessage("Started annotation processing")
        val generatedAdapters = env.getElementsAnnotatedWith(AutoGsonAdapter::class.java)

        // Handle the standard type adapters.
        val adapterGenerator = AutoGsonAdapterGenerator(processingEnv)

        val autoGsonAdapterResults = ArrayList<HandleResult>()
        for (element in generatedAdapters) {
            printMessage("Generating TypeAdapter ($element)")

            try {
                autoGsonAdapterResults.add(adapterGenerator.handle(element as TypeElement))
            } catch (e: ProcessingException) {
                printError(e.message, e.element ?: element)
                return false
            }

        }

        if (autoGsonAdapterResults.isNotEmpty()) {
            if (!TypeAdapterLoaderGenerator(processingEnv).generate(autoGsonAdapterResults)) {
                printError("Error while generating TypeAdapterFactory")
                return false
            }
        }

        printMessage("Finished annotation processing")
        println()

        return false
    }

    private fun printMessage(message: String) {
        println(LOG_PREFIX + message)
    }

    private fun printError(message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message)
    }

    private fun printError(message: String, element: Element) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message, element)
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val supportedTypes = LinkedHashSet<String>()
        supportedTypes.add(AutoGsonAdapter::class.java.canonicalName)
        return supportedTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    companion object {
        private val LOG_PREFIX = "Gson Path: "
    }

}