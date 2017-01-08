package gsonpath.model

import com.squareup.javapoet.ClassName

class InterfaceInfo(val parentClassName: ClassName, internal val fieldInfo: Array<InterfaceFieldInfo>)
