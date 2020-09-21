package gsonpath

import gsonpath.adapter.util.ElementAndAnnotation
import gsonpath.annotation.AutoGsonAdapterFactory
import javax.lang.model.element.TypeElement

class LazyFactoryMetadata(
        private val _elementAndAnnotation: ElementAndAnnotation<AutoGsonAdapterFactory>?) {

    private val elementAndAnnotation: ElementAndAnnotation<AutoGsonAdapterFactory>
        get() {
            return _elementAndAnnotation ?: throw ProcessingException("An interface annotated with" +
                    " @AutoGsonAdapterFactory (that directly extends JsonAdapter.Factory) must exist " +
                    "before the annotation processor can succeed. " +
                    "See the AutoGsonAdapterFactory annotation for further details.")
        }

    val annotation: AutoGsonAdapterFactory
        get() = elementAndAnnotation.annotation

    val element: TypeElement
        get() = elementAndAnnotation.element
}
