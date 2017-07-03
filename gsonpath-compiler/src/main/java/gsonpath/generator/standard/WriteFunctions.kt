package gsonpath.generator.standard

import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.compiler.GSON_SUPPORTED_CLASSES
import gsonpath.compiler.addComment
import gsonpath.compiler.addNewLine
import gsonpath.model.GsonArray
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import java.io.IOException
import javax.lang.model.element.Modifier

/**
 * public void write(JsonWriter out, ImageSizes value) throws IOException {
 */
@Throws(ProcessingException::class)
fun createWriteMethod(elementClassName: ClassName,
                      rootElements: GsonObject,
                      serializeNulls: Boolean): MethodSpec {

    val writeMethod = MethodSpec.methodBuilder("write")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(JsonWriter::class.java, "out")
            .addParameter(elementClassName, "value")
            .addException(IOException::class.java)

    val codeBlock = CodeBlock.builder()

            // Initial block which prevents nulls being accessed.
            .beginControlFlow("if (value == null)")
            .addStatement("out.nullValue()")
            .addStatement("return")
            .endControlFlow()

            .addNewLine()
            .addComment("Begin")

    writeGsonFieldWriter(codeBlock, rootElements, "", serializeNulls, 0, true)

    writeMethod.addCode(codeBlock.build())
    return writeMethod.build()
}

@Throws(ProcessingException::class)
private fun writeGsonFieldWriter(codeBlock: CodeBlock.Builder,
                                 jsonMapping: GsonObject,
                                 currentPath: String,
                                 serializeNulls: Boolean,
                                 currentFieldCount: Int,
                                 writeKeyName: Boolean): Int {

    codeBlock.addStatement("out.beginObject()")

    val overallFieldCount: Int = jsonMapping.entries().fold(currentFieldCount) { previousFieldCount, (key, value) ->
        return@fold when (value) {
            is GsonField -> {
                handleGsonField(codeBlock, value, key, serializeNulls, writeKeyName, previousFieldCount)
            }

            is GsonArray -> {
                handleGsonArray(codeBlock, value, currentPath, key, serializeNulls, previousFieldCount)
            }

            is GsonObject -> {
                if (value.size() > 0) {
                    val newPath: String =
                            if (currentPath.isNotEmpty()) {
                                "$currentPath.$key"
                            } else {
                                key
                            }

                    // Add a comment mentioning what nested object we are current pointing at.
                    codeBlock.addNewLine()
                            .addComment("Begin Object: '$newPath'")
                            .addStatement("""out.name("$key")""")

                    writeGsonFieldWriter(codeBlock, value, newPath, serializeNulls, previousFieldCount, writeKeyName)
                } else {
                    previousFieldCount
                }
            }
        }
    }

    codeBlock.addComment("End Object: '$currentPath'")
            .addStatement("out.endObject()")

    return overallFieldCount
}

private fun handleGsonArray(codeBlock: CodeBlock.Builder, gsonArray: GsonArray, currentPath: String, key: String,
                            serializeNulls: Boolean, currentFieldCount: Int): Int {

    codeBlock.addNewLine()
            .addComment("Begin Array: '$currentPath'")
            .addStatement("""out.name("$key")""")
            .addStatement("out.beginArray()")
            .addNewLine()

    val maxIndex: Int = gsonArray.entries().map { it.key }.max()!!

    val newFieldCount =
            (0..maxIndex).fold(currentFieldCount) { previousFieldCount, arrayIndex ->
                val arrayElement = gsonArray[arrayIndex]

                val newPath: String =
                        if (currentPath.isEmpty()) {
                            "$key[$arrayIndex]"
                        } else {
                            "$currentPath.$key[$arrayIndex]"
                        }

                if (arrayElement == null) {
                    // Add any empty array items if required.
                    codeBlock.add("out.nullValue(); // Set Value: '$newPath'")
                            .addNewLine()

                    return@fold previousFieldCount
                }

                if (arrayElement is GsonField) {
                    codeBlock.addNewLine()
                            .addComment("Set Value: '$newPath'")

                    return@fold handleGsonField(codeBlock, arrayElement, key, serializeNulls, false, previousFieldCount)

                } else {
                    codeBlock.addNewLine()
                            .addComment("Begin Object: '$newPath'")

                    return@fold writeGsonFieldWriter(codeBlock, arrayElement as GsonObject, newPath, serializeNulls, previousFieldCount, true)
                }
            }

    codeBlock.addComment("End Array: '$key'")
            .addStatement("out.endArray()")

    return newFieldCount
}

private fun handleGsonField(codeBlock: CodeBlock.Builder, gsonField: GsonField, key: String,
                            serializeNulls: Boolean, writeKeyName: Boolean,
                            currentFieldCount: Int): Int {

    val fieldInfo = gsonField.fieldInfo

    // Make sure the field's annotations don't have any problems.
    validateFieldAnnotations(fieldInfo)

    val fieldTypeName = fieldInfo.typeName
    val isPrimitive = fieldTypeName.isPrimitive

    val objectName = "obj" + currentFieldCount

    codeBlock.addStatement("\$T $objectName = value.${fieldInfo.fieldName}", fieldTypeName)

    if (writeKeyName) {
        // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
        if (!isPrimitive && !serializeNulls) {
            codeBlock.beginControlFlow("if ($objectName != null)")
        }
        codeBlock.addStatement("""out.name("$key")""")
    }

    // Since we are serializing nulls, we defer the if-statement until after the name is written.
    if (!isPrimitive && serializeNulls) {
        codeBlock.beginControlFlow("if ($objectName != null)")
    }

    if (isPrimitive || GSON_SUPPORTED_CLASSES.contains(fieldTypeName)) {

        codeBlock.addStatement("out.value($objectName)")

    } else {
        val adapterName: String

        if (fieldTypeName is ParameterizedTypeName) {
            // This is a generic type
            adapterName = "new com.google.gson.reflect.TypeToken<$fieldTypeName>(){}"

        } else {
            adapterName = fieldTypeName.toString() + ".class"
        }

        val subTypeAnnotation = fieldInfo.getAnnotation(GsonSubtype::class.java)
        val writeLine =
                if (subTypeAnnotation != null) {
                    // If this field uses a subtype annotation, we use the type adapter subclasses instead of gson.
                    "${getSubTypeGetterName(gsonField)}().write(out, $objectName)"
                } else {
                    // Otherwise we request the type adapter from gson.
                    "mGson.getAdapter($adapterName).write(out, $objectName)"
                }

        codeBlock.addStatement(writeLine)
    }

    // If we are serializing nulls, we need to ensure we output it here.
    if (!isPrimitive) {
        if (serializeNulls) {
            codeBlock.nextControlFlow("else")
                    .addStatement("out.nullValue()")
        }
        codeBlock.endControlFlow()
    }
    codeBlock.addNewLine()

    return currentFieldCount + 1
}