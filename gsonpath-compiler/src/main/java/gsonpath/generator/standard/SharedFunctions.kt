package gsonpath.generator.standard

import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.model.FieldInfo
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

fun validateFieldAnnotations(fieldInfo: FieldInfo) {
    // For now, we only ensure that the flatten annotation is only added to a String.
    if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
        return
    }

    if (fieldInfo.typeName != CLASS_NAME_STRING) {
        throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
    }
}

fun findNonEmptyConstructor(processingEnv: ProcessingEnvironment, modelElement: TypeElement): ExecutableType? {
    return processingEnv.elementUtils.getAllMembers(modelElement)
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .map { (it.asType() as ExecutableType) }
            .find { it.parameterTypes.size > 0 }
}