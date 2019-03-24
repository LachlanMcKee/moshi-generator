package gsonpath.adapter

import gsonpath.AutoGsonAdapter
import gsonpath.adapter.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.dependencies.Dependencies
import gsonpath.generator.HandleResult
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object StandardAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        return getAnnotatedModelElements<AutoGsonAdapter>(env, annotations, listOf(ElementKind.CLASS, ElementKind.INTERFACE))
                .onEach { logger.printMessage("Generating TypeAdapter (${it.element})") }
                .map { dependencies.autoGsonAdapterGenerator.handle(it.element, it.annotation) }
    }

}