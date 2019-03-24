package gsonpath.adapter

import gsonpath.dependencies.Dependencies
import gsonpath.generator.HandleResult
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

interface AdapterFactory {
    fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult>
}