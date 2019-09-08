package gsonpath.model

import javax.lang.model.element.Element

interface FieldInfo {
    val fieldType: FieldType

    /**
     * The raw annotation processor field element.
     * This should mainly be used for reporting errors back to the user.
     */
    val element: Element
}