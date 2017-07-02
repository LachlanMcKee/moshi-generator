package gsonpath

import gsonpath.generator.HandleResult
import gsonpath.generator.standard.AutoGsonAdapterGenerator
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import gsonpath.compiler.GsonPathExtension

open class GsonProcessorImpl : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null || annotations.isEmpty()) {
            return false
        }

        // Load any extensions that are also available at compile time.
        println()
        val extensions: List<GsonPathExtension> =
                try {
                    ServiceLoader.load(GsonPathExtension::class.java, javaClass.classLoader).toList()

                } catch (t: Throwable) {
                    printError("Failed to load one or more GsonPath extensions. Cause: ${t.message}")
                    return false
                }

        // Print the extensions for auditing purposes.
        extensions.forEach {
            printMessage("Extension found: " + it.extensionName)
        }

        println()
        printMessage("Started annotation processing")
        val generatedAdapters = env.getElementsAnnotatedWith(AutoGsonAdapter::class.java)

        // Handle the standard type adapters.
        val adapterGenerator = AutoGsonAdapterGenerator(processingEnv)

        val autoGsonAdapterResults: List<HandleResult> =
                generatedAdapters.map {
                    printMessage("Generating TypeAdapter ($it)")

                    try {
                        adapterGenerator.handle(it as TypeElement, extensions)
                    } catch (e: ProcessingException) {
                        printError(e.message, e.element ?: it)
                        return false
                    }
                }

        if (autoGsonAdapterResults.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            if (gsonPathFactories.count() == 0) {
                printError("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                        "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                        "See the AutoGsonAdapterFactory annotation for further details.")
                return false
            }

            if (gsonPathFactories.count() > 1) {
                printError("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                return false
            }

            val factoryElement = gsonPathFactories.first()
            try {
                if (!TypeAdapterFactoryGenerator(processingEnv).generate(factoryElement as TypeElement, autoGsonAdapterResults)) {
                    printError("Error while generating TypeAdapterFactory", factoryElement)
                    return false
                }
            } catch (e: ProcessingException) {
                printError(e.message, e.element ?: factoryElement)
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
        return setOf(
                AutoGsonAdapter::class.java.canonicalName,
                AutoGsonAdapterFactory::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    companion object {
        private val LOG_PREFIX = "Gson Path: "
    }

}