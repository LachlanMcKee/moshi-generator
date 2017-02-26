package gsonpath.generator.adapter.standard

import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.generator.adapter.GSON_SUPPORTED_CLASSES
import gsonpath.generator.adapter.addComment
import gsonpath.generator.adapter.addNewLine
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

    writeGsonFieldWriter(0, codeBlock, rootElements, "", serializeNulls, mutableListOf())

    writeMethod.addCode(codeBlock.build())
    return writeMethod.build()
}

@Throws(ProcessingException::class)
private fun writeGsonFieldWriter(fieldDepth: Int,
                                 codeBlock: CodeBlock.Builder,
                                 jsonMapping: GsonObject,
                                 currentPath: String,
                                 serializeNulls: Boolean,
                                 arrayNameCounterList: MutableList<Boolean>) {

    codeBlock.addStatement("out.beginObject()")

    for (key in jsonMapping.keySet()) {
        val value = jsonMapping[key]

        if (value is GsonField) {
            val fieldInfo = value.fieldInfo

            // Make sure the field's annotations don't have any problems.
            validateFieldAnnotations(fieldInfo)

            val fieldTypeName = fieldInfo.typeName
            val isPrimitive = fieldTypeName.isPrimitive

            val objectName = "obj" + arrayNameCounterList.size
            arrayNameCounterList.add(true)

            codeBlock.addStatement("\$T $objectName = value.${fieldInfo.fieldName}", fieldTypeName)

            // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
            if (!isPrimitive && !serializeNulls) {
                codeBlock.beginControlFlow("if ($objectName != null)")
            }
            codeBlock.addStatement("""out.name("$key")""")

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
                            "${getSubTypeGetterName(value)}().write(out, $objectName)"
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

        } else {
            val nextLevelMap = value as GsonObject
            if (nextLevelMap.size() > 0) {
                val newPath: String
                if (currentPath.isNotEmpty()) {
                    newPath = currentPath + "" + key
                } else {
                    newPath = key
                }

                // Add a comment mentioning what nested object we are current pointing at.
                codeBlock.addNewLine()
                        .addComment("Begin $newPath")
                        .addStatement("""out.name("$key")""")

                writeGsonFieldWriter(fieldDepth + 1, codeBlock, nextLevelMap, newPath, serializeNulls, arrayNameCounterList)
            }
        }
    }

    codeBlock.addComment("End $currentPath")
            .addStatement("out.endObject()")
}