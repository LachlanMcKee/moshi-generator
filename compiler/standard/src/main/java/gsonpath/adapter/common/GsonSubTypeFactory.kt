package gsonpath.adapter.common

import com.squareup.javapoet.*
import com.squareup.moshi.JsonAdapter
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.Constants.MOSHI
import gsonpath.adapter.standard.extension.addException
import gsonpath.util.*

object GsonSubTypeFactory {

    fun createSubTypeMetadata(
            elementTypeName: TypeName,
            subTypeMetadata: SubTypeMetadata): GsonSubTypeResult {

        return GsonSubTypeResult(
                readMethodSpecs = createReadMethod(subTypeMetadata, elementTypeName),
                writeMethodSpecs = createWriteMethod(elementTypeName)
        )
    }

    /**
     * The read method deserializes the entire json object which is inefficient, however it unfortunately the only way
     * to guarantee that the 'type' field is read early enough.
     *
     * Once the object is memory, the type field is located, and then the correct adapter is hopefully found and
     * delegated to. If not, the deserializer may return null, use a default deserializer, or throw an exception
     * depending on the GsonSubtype annotation settings.
     */
    private fun createReadMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName) = AdapterMethodBuilder.createReadMethodBuilder(rawTypeName).applyAndBuild {

        code {
            val mapWithObjectTypeName = ParameterizedTypeName.get(Map::class.java, Object::class.java, String::class.java)
            createVariable(mapWithObjectTypeName,
                    JSON_ELEMENT,
                    "(\$T) reader.readJsonValue()",
                    mapWithObjectTypeName)

            val fieldInfo = subTypeMetadata.gsonSubTypeFieldInfo.apply {
                forEach {
                    newLine()
                    addVariable(it, rawTypeName)
                }
                newLine()
            }

            createVariable(ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName)),
                    DELEGATE_CLASS,
                    "\$T.${subTypeMetadata.classGetterMethodName}(${fieldInfo.joinToString { it.variableName }})",
                    rawTypeName)

            `if`("$DELEGATE_CLASS == ${Constants.NULL}") {
                `return`(Constants.NULL)
            }

            createVariable(rawTypeName, RESULT, "$MOSHI.adapter($DELEGATE_CLASS).fromJsonValue($JSON_ELEMENT)")

            `return`(RESULT)
        }
    }

    private fun CodeBlock.Builder.addVariable(fieldInfo: GsonSubTypeFieldInfo, rawTypeName: TypeName) {
        val jsonElementName = fieldInfo.variableName + "_jsonElement"
        createVariable(Object::class.java,
                jsonElementName,
                "$JSON_ELEMENT.get(\"${fieldInfo.jsonKey}\")")

        addStatement("final \$T ${fieldInfo.variableName}", fieldInfo.parameterTypeName)

        ifWithoutClose("$jsonElementName == ${Constants.NULL}") {
            if (fieldInfo.nullable) {
                assign(fieldInfo.variableName, "null")
            } else {
                addException("cannot deserialize $rawTypeName because the subtype field " +
                        "'${fieldInfo.jsonKey}' is either null or does not exist.")
            }
        }
        `else` {
            val adapterName =
                    if (fieldInfo.parameterTypeName is ParameterizedTypeName)
                        "new com.google.gson.reflect.TypeToken<\$T>(){}" // This is a generic type
                    else
                        "\$T.class"

            assign(fieldInfo.variableName,
                    "$MOSHI.adapter($adapterName).fromJsonValue($jsonElementName)",
                    fieldInfo.parameterTypeName)
        }
    }

    /**
     * The write method is substantially simpler, as we do not to consume an entire json object.
     */
    private fun createWriteMethod(
            rawTypeName: TypeName) = AdapterMethodBuilder.createWriteMethodBuilder(rawTypeName).applyAndBuild {

        code {
            createVariable(JsonAdapter::class.java, DELEGATE_ADAPTER, "$MOSHI.adapter(${Constants.VALUE}.getClass())")
            addStatement("$DELEGATE_ADAPTER.toJson(${Constants.WRITER}, ${Constants.VALUE})")
        }
    }

    private const val JSON_ELEMENT = "jsonElement"
    private const val DELEGATE_CLASS = "delegateClass"
    private const val DELEGATE_ADAPTER = "delegateAdapter"
    private const val RESULT = "result"
}
