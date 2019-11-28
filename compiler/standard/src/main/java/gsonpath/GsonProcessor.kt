package gsonpath

import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.Dependencies
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

open class GsonProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }

        val logger = Logger(processingEnv)

        try {
            processInternal(annotations, env, logger)
        } catch (e: ProcessingException) {
            logger.printError(e.message, e.element)
        }

        return true
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        println()
        logger.printMessage("Started annotation processing")

        val dependencies = DependencyFactory.create(processingEnv)
        val autoGsonAdapterResults = StandardAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
                .plus(SubTypeAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies))
                .plus(EnumAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies))

        generateTypeAdapterFactories(env, dependencies, autoGsonAdapterResults)

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun generateTypeAdapterFactories(
            env: RoundEnvironment,
            dependencies: Dependencies,
            autoGsonAdapterResults: List<AdapterGenerationResult>) {

        if (autoGsonAdapterResults.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            when {
                gsonPathFactories.count() == 0 -> {
                    throw ProcessingException("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                            "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                            "See the AutoGsonAdapterFactory annotation for further details.")
                }
                gsonPathFactories.count() > 1 -> {
                    throw ProcessingException("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                }
                else -> {
                    val factoryElement = gsonPathFactories.first()
                    dependencies.typeAdapterFactoryGenerator.generate(factoryElement as TypeElement, autoGsonAdapterResults)
                }
            }
        }
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return incrementalMetadata
                ?.let { (customAnnotations) ->
                    customAnnotations.plus(setOf(
                            AutoGsonAdapterFactory::class.java.canonicalName,
                            AutoGsonAdapter::class.java.canonicalName,
                            GsonSubtype::class.java.canonicalName
                    ))
                }
                ?: setOf("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): Set<String> {
        return if (incrementalMetadata != null) {
            setOf("org.gradle.annotation.processing.aggregating")
        } else {
            emptySet()
        }
    }

    private val incrementalMetadata: IncrementalMetadata? by lazy {
        if ("true" != processingEnv.options[OPTION_INCREMENTAL]) {
            null
        } else {
            processingEnv.options[OPTION_ADDITIONAL_ANNOTATIONS]
                    ?.let { IncrementalMetadata(it.split(",").toSet()) }
                    ?: IncrementalMetadata(emptySet())
        }
    }

    private data class IncrementalMetadata(val customAnnotations: Set<String>)

    private companion object {
        private const val OPTION_INCREMENTAL = "gsonpath.incremental"
        private const val OPTION_ADDITIONAL_ANNOTATIONS = "gsonpath.additionalAnnotations"
    }
}