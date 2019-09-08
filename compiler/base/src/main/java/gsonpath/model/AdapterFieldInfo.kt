package gsonpath.model

/**
 * Contains important information about a class field within a class annotated with the AutoGsonAdapter annotation.
 */
interface AdapterFieldInfo : FieldInfo {
    /**
     * The name of the class that field is contained within.
     */
    val parentClassName: String

    /**
     * Searches for an annotation that is attached to the field.
     */
    fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

    /**
     * The name of the field this [AdapterFieldInfo] represents.
     */
    val fieldName: String

    /**
     * The mechanism for accessing the field from the class.
     *
     * This allows the library to either access the variable via an exposed field, or potentially a getter instead.
     */
    val fieldAccessor: String

    /**
     * Returns the raw names of all annotations attached to the field.
     *
     * This is useful for finding annotations without having the annotation class included in the
     * annotation processor library.
     */
    val annotationNames: List<String>

    /**
     * Whether the field has a default value assigned.
     */
    val hasDefaultValue: Boolean
}
