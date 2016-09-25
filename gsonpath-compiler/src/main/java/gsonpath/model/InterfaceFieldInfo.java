package gsonpath.model;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

public class InterfaceFieldInfo {
    final Element methodElement;
    final TypeName typeName;
    public final String fieldName;

    public InterfaceFieldInfo(Element methodElement, TypeName typeName, String fieldName) {
        this.methodElement = methodElement;
        this.typeName = typeName;
        this.fieldName = fieldName;
    }
}
