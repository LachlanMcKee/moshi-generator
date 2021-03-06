package gsonpath.adapter.standard.adapter.write

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants.GET_ADAPTER
import gsonpath.adapter.Constants.GSON
import gsonpath.adapter.Constants.NULL
import gsonpath.adapter.Constants.OUT
import gsonpath.adapter.Constants.VALUE
import gsonpath.adapter.standard.extension.ExtensionsHandler
import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonObject
import gsonpath.internal.GsonUtil
import gsonpath.model.FieldType
import gsonpath.util.*

/**
 * public void write(JsonWriter out, ImageSizes value) throws IOException {
 */
class WriteFunctions(private val extensionsHandler: ExtensionsHandler) {
    @Throws(ProcessingException::class)
    fun handleWrite(typeSpecBuilder: TypeSpec.Builder, params: WriteParams) {
        typeSpecBuilder.addMethod(AdapterMethodBuilder.createWriteMethodBuilder(params.elementClassName).applyAndBuild {
            code {
                comment("Begin")
                writeGsonFieldWriter(params.rootElements, params.serializeNulls, true)
            }
        })
    }

    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.writeGsonFieldWriter(
            jsonMapping: GsonObject,
            serializeNulls: Boolean,
            writeKeyName: Boolean,
            currentPath: String = "",
            currentFieldCount: Int = 0): Int {

        addStatement("$OUT.beginObject()")

        return jsonMapping.entries()
                .fold(currentFieldCount) { fieldCount, (key, value) ->
                    when (value) {
                        is GsonObject ->
                            handleObject(value, fieldCount, serializeNulls, currentPath, key, writeKeyName)

                        is GsonField ->
                            handleField(value, fieldCount, serializeNulls, key, writeKeyName)

                        is GsonArray ->
                            handleArray(value, fieldCount, serializeNulls, currentPath, key)
                    }
                }
                .also {
                    comment("End $currentPath")
                    addStatement("$OUT.endObject()")
                }
    }

    private fun CodeBlock.Builder.handleObject(
            value: GsonObject,
            fieldCount: Int,
            serializeNulls: Boolean,
            currentPath: String,
            key: String,
            writeKeyName: Boolean): Int {

        if (value.size() == 0) {
            return fieldCount
        }

        val newPath = currentPath + key

        // Add a comment mentioning what nested object we are current pointing at.
        newLine()
        comment("Begin $newPath")
        addStatement("""$OUT.name("$key")""")

        return writeGsonFieldWriter(value, serializeNulls, writeKeyName, newPath, fieldCount)
    }

    private fun CodeBlock.Builder.handleField(
            value: GsonField,
            fieldCount: Int,
            serializeNulls: Boolean,
            key: String,
            writeKeyName: Boolean): Int {

        val fieldInfo = value.fieldInfo
        val fieldTypeName = fieldInfo.fieldType.typeName
        val isPrimitive = fieldInfo.fieldType is FieldType.Primitive
        val fieldAccessor = fieldInfo.fieldAccessor
        val objectName = "obj$fieldCount"

        createVariable(fieldTypeName, objectName, "$VALUE.$fieldAccessor")

        if (isPrimitive) {
            if (writeKeyName) {
                addEscapedStatement("""$OUT.name("$key")""")
            }
            writeField(value, objectName, fieldTypeName)
        } else {
            if (serializeNulls) {
                // Since we are serializing nulls, we defer the if-statement until after the name is written.
                if (writeKeyName) {
                    addEscapedStatement("""$OUT.name("$key")""")
                }
                ifWithoutClose("$objectName != $NULL") {
                    writeField(value, objectName, fieldTypeName)
                }
                `else` {
                    addStatement("$OUT.nullValue()")
                }
            } else {
                `if`("$objectName != $NULL") {
                    if (writeKeyName) {
                        addEscapedStatement("""$OUT.name("$key")""")
                    }
                    writeField(value, objectName, fieldTypeName)
                }
            }
        }
        newLine()

        return fieldCount + 1
    }

    private fun CodeBlock.Builder.handleArray(
            gsonArray: GsonArray,
            currentFieldCount: Int,
            serializeNulls: Boolean,
            currentPath: String,
            key: String): Int {

        newLine()
        comment("Begin Array: '$currentPath.$key'")
        addStatement("""out.name("$key")""")
        addStatement("out.beginArray()")
        newLine()

        return (0..gsonArray.maxIndex)
                .fold(currentFieldCount) { previousFieldCount, arrayIndex ->
                    val newPath: String =
                            if (currentPath.isEmpty()) {
                                "$key[$arrayIndex]"
                            } else {
                                "$currentPath.$key[$arrayIndex]"
                            }

                    when (val arrayElement = gsonArray[arrayIndex]) {
                        null -> {
                            // Add any empty array items if required.
                            add("out.nullValue(); // Set Value: '$newPath'")
                            newLine()
                            previousFieldCount
                        }
                        is GsonField -> {
                            newLine()
                            comment("Set Value: '$newPath'")
                            handleField(arrayElement, previousFieldCount, serializeNulls, key, false)
                        }
                        is GsonObject -> {
                            newLine()
                            comment("Begin Object: '$newPath'")
                            writeGsonFieldWriter(arrayElement, serializeNulls, true, newPath, previousFieldCount)
                        }
                    }
                }
                .also {
                    comment("End Array: '$key'")
                    addStatement("out.endArray()")
                }
    }

    private fun CodeBlock.Builder.writeField(value: GsonField, objectName: String, fieldTypeName: TypeName) {
        when {
            extensionsHandler.canHandleFieldWrite(value, objectName) -> {
                extensionsHandler.executeFieldWrite(value, objectName) { extensionName, writeResult ->
                    comment("Extension (Write) - $extensionName")
                    add(writeResult.codeBlock)
                }
            }
            fieldTypeName is ParameterizedTypeName -> {
                addStatement("$GET_ADAPTER(new com.google.gson.reflect.TypeToken<\$T>(){}).write($OUT, $objectName)", fieldTypeName.box())
            }
            else -> {
                if (fieldTypeName.isPrimitive) {
                    addStatement("$GET_ADAPTER(\$T.class).write($OUT, $objectName)", fieldTypeName.box())

                } else {
                    addStatement("\$T.writeWithGenericAdapter($GSON, $objectName.getClass(), $OUT, $objectName)", GsonUtil::class.java)
                }
            }
        }
    }
}