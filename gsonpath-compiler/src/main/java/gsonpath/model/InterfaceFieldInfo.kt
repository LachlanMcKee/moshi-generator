package gsonpath.model

import com.squareup.javapoet.TypeName

import javax.lang.model.element.Element

class InterfaceFieldInfo(internal val methodElement: Element, internal val typeName: TypeName, val fieldName: String)
