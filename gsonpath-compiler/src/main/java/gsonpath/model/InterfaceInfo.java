package gsonpath.model;

import com.squareup.javapoet.ClassName;

public class InterfaceInfo {
    public final ClassName parentClassName;
    final InterfaceFieldInfo[] fieldInfo;

    public InterfaceInfo(ClassName parentClassName, InterfaceFieldInfo[] fieldInfo) {
        this.parentClassName = parentClassName;
        this.fieldInfo = fieldInfo;
    }
}
