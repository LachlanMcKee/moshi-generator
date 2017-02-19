package gsonpath.generator.adapter.standard

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.internal.StrictArrayTypeAdapter
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.model.GsonObjectTreeFactory
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)

/**
 * Creates the code required for subtype adapters for any fields that use the GsonSubtype annotation.
 */
fun addSubTypeTypeAdapters(processingEnv: ProcessingEnvironment, typeSpecBuilder: TypeSpec.Builder, rootElements: GsonObject) {
    val flattenedFields = GsonObjectTreeFactory().getFlattenedFieldsFromGsonObject(rootElements)

    flattenedFields.forEach { gsonField ->
        // Ignore any fields that do not have a GsonSubtype annotation.
        val subTypeAnnotation = gsonField.fieldInfo.getAnnotation(GsonSubtype::class.java) ?: return

        typeSpecBuilder.addField(arrayTypeAdapterClassName, getSubTypeAdapterVariableName(gsonField), Modifier.PRIVATE)

        createGetter(typeSpecBuilder, gsonField)
        createSubTypeAdapter(processingEnv, typeSpecBuilder, gsonField, subTypeAnnotation)
    }
}

/**
 * Obtains the name of the subtype adapter getter method contained within the root level class.
 */
fun getSubTypeGetterName(gsonField: GsonField): String {
    val variableName = getSubTypeAdapterVariableName(gsonField)
    return "get${variableName[0].toUpperCase()}${variableName.substring(1)}"
}

/**
 * Creates the getter for the type adapter.
 * This implementration lazily loads, and then cached the result for subsequent usages.
 */
private fun createGetter(typeSpecBuilder: TypeSpec.Builder, gsonField: GsonField) {
    val variableName = getSubTypeAdapterVariableName(gsonField)

    typeSpecBuilder.addMethod(MethodSpec.methodBuilder(getSubTypeGetterName(gsonField))
            .addModifiers(Modifier.PRIVATE)
            .returns(arrayTypeAdapterClassName)

            .addCode(CodeBlock.builder()
                    .beginControlFlow("if ($variableName == null)")

                    .addStatement("$variableName = new \$T<>(new ${getSubTypeAdapterClassName(gsonField)}(mGson), \$T.class)",
                            arrayTypeAdapterClassName, getRawType(gsonField))

                    .endControlFlow()
                    .addStatement("return $variableName")
                    .build())
            .build())
}

/**
 * Obtains the actual type name that is either contained within the array or the list.
 * e.g. for 'String[]' or 'List<String>' the returned type name is 'String'
 */
private fun getRawType(gsonField: GsonField): TypeName {
    val typeName = gsonField.fieldInfo.typeName
    return when (typeName) {
        is ArrayTypeName -> typeName.componentType
        is ParameterizedTypeName -> typeName.rawType

        else -> throw ProcessingException("Unexpected type found for GsonSubtype field, ensure you either use " +
                "an array, or a List class.")
    }
}

/**
 * Obtains the name of the subtype adapter field contained within the root level class.
 */
private fun getSubTypeAdapterVariableName(gsonField: GsonField): String {
    return "${gsonField.fieldInfo.fieldName}GsonSubtype"
}

/**
 * Obtains the class name of the subtype adapter contained within the root level class.
 */
private fun getSubTypeAdapterClassName(gsonField: GsonField): String {
    return gsonField.fieldInfo.fieldName[0].toUpperCase() + gsonField.fieldInfo.fieldName.substring(1) + "GsonSubtype"
}

/**
 * Creates the gson 'subtype' type adapter inside of the root level class.
 * <p>
 * Only gson fields that are annotated with 'GsonSubtype' should invoke this method
 */
private fun createSubTypeAdapter(processingEnv: ProcessingEnvironment, typeSpecBuilder: TypeSpec.Builder, gsonField: GsonField, annotation: GsonSubtype) {
    val rawTypeName = getRawType(gsonField)

    val subTypeAdapterBuilder = TypeSpec.classBuilder(getSubTypeAdapterClassName(gsonField))
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), rawTypeName))

    // Create the type adapter delegate map.
    val typeAdapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(rawTypeName))
    val classConstainedType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName))

    // Check which key types are being used.
    val keyType: SubTypeKeyType =
            when {
                (annotation.stringKeys.isNotEmpty()) -> SubTypeKeyType.STRING
                (annotation.integerKeys.isNotEmpty()) -> SubTypeKeyType.INTEGER
                (annotation.booleanKeys.isNotEmpty()) -> SubTypeKeyType.BOOLEAN
                else -> throw ProcessingException("Keys must be specified for the GsonSubType")
            }

    val valueMapClassName =
            when (keyType) {
                SubTypeKeyType.STRING -> ClassName.get(String::class.java)
                SubTypeKeyType.INTEGER -> TypeName.get(Int::class.java).box()
                SubTypeKeyType.BOOLEAN -> TypeName.get(Boolean::class.java).box()
            }

    subTypeAdapterBuilder.addField(
            FieldSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Map::class.java), valueMapClassName, typeAdapterType), "typeAdaptersDelegatedByValueMap")
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build())

    subTypeAdapterBuilder.addField(
            FieldSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Map::class.java), classConstainedType, typeAdapterType), "typeAdaptersDelegatedByClassMap")
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build())

    // Add the constructor
    val constructorBuilder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(Gson::class.java, "gson")

            .addStatement("typeAdaptersDelegatedByValueMap = new java.util.HashMap<>()")
            .addStatement("typeAdaptersDelegatedByClassMap = new java.util.HashMap<>()")

    //
    // Convert the provided keys into a unified type. Unfortunately due to how annotations work, this isn't
    // as clean as it could be.
    //
    val genericGsonSubTypeKeys: List<GsonSubTypeKeyAndClass> =
            when (keyType) {
                SubTypeKeyType.STRING ->
                    annotation.stringKeys.map { it ->
                        try {
                            it.subtype
                            throw ProcessingException("Unexpected annotation processing defect while obtaining class.")
                        } catch (mte: MirroredTypeException) {
                            GsonSubTypeKeyAndClass("\"${it.key}\"", mte.typeMirror)
                        }
                    }

                SubTypeKeyType.INTEGER ->
                    annotation.integerKeys.map { it ->
                        try {
                            it.subtype
                            throw ProcessingException("Unexpected annotation processing defect while obtaining class.")
                        } catch (mte: MirroredTypeException) {
                            GsonSubTypeKeyAndClass("${it.key}", mte.typeMirror)
                        }
                    }

                SubTypeKeyType.BOOLEAN ->
                    annotation.booleanKeys.map { it ->
                        try {
                            it.subtype
                            throw ProcessingException("Unexpected annotation processing defect while obtaining class.")
                        } catch (mte: MirroredTypeException) {
                            GsonSubTypeKeyAndClass("${it.key}", mte.typeMirror)
                        }
                    }
            }

    // Instantiate each subtype delegated adapter
    genericGsonSubTypeKeys.forEach {
        val subtypeElement = processingEnv.typeUtils.asElement(it.clazzTypeMirror)

        constructorBuilder.addCode("\n")
        constructorBuilder.addStatement("typeAdaptersDelegatedByValueMap.put(${it.key}, gson.getAdapter($subtypeElement.class))")
        constructorBuilder.addStatement("typeAdaptersDelegatedByClassMap.put($subtypeElement.class, gson.getAdapter($subtypeElement.class))")
    }

    subTypeAdapterBuilder.addMethod(constructorBuilder.build())

    // Add the read method.
    val readMethod = MethodSpec.methodBuilder("read")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(rawTypeName)
            .addParameter(JsonReader::class.java, "in")
            .addException(IOException::class.java)

    //
    // The read method deserializes the entire json object which is inefficient, however it unfortunately the only way
    // to guarantee that the 'type' field is read early enough.
    //
    // Once the object is memory, the type field is located, and then the correct adapter is hopefully found and delegated
    // to. If not, the deserializer may return null, or throw an exception depending on the GsonSubtype annotation settings.
    //
    readMethod.addStatement("\$T jsonElement = \$T.parse(in)", JsonElement::class.java, Streams::class.java)
            .addStatement("\$T typeValueJsonElement = jsonElement.getAsJsonObject().remove(\"${annotation.fieldName}\")", JsonElement::class.java)

            .beginControlFlow("if (typeValueJsonElement == null)")
            .addStatement("throw new \$T(\"cannot deserialize $rawTypeName because it does not define a field named '${annotation.fieldName}'\")",
                    JsonParseException::class.java)

            .endControlFlow()

    // Obtain the value using the correct type.
    when (keyType) {
        SubTypeKeyType.STRING -> readMethod.addStatement("java.lang.String value = typeValueJsonElement.getAsString()")
        SubTypeKeyType.INTEGER -> readMethod.addStatement("int value = typeValueJsonElement.getAsInt()")
        SubTypeKeyType.BOOLEAN -> readMethod.addStatement("boolean value = typeValueJsonElement.getAsBoolean()")
    }

    readMethod.addStatement("\$T<? extends $rawTypeName> delegate = typeAdaptersDelegatedByValueMap.get(value)", TypeAdapter::class.java)
            .beginControlFlow("if (delegate == null)")
            .addStatement("return null")
            .endControlFlow()

            .addStatement("return delegate.fromJsonTree(jsonElement)")

    subTypeAdapterBuilder.addMethod(readMethod.build())

    //
    // Add the write method
    // The write method is substantially simpler, as we do not to consume an entire json object.
    //
    subTypeAdapterBuilder.addMethod(MethodSpec.methodBuilder("write")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(JsonWriter::class.java, "out")
            .addParameter(rawTypeName, "value")
            .addException(IOException::class.java)

            .beginControlFlow("if (value == null)")
            .addStatement("out.nullValue()")
            .addStatement("return")
            .endControlFlow()

            .addStatement("\$T delegate = typeAdaptersDelegatedByClassMap.get(value.getClass())", TypeAdapter::class.java)
            .addStatement("delegate.write(out, value)", typeAdapterType)
            .build())

    // Add the new subtype type adapter to the root class.
    typeSpecBuilder.addType(subTypeAdapterBuilder.build())
}

/**
 * A data class that is used to convert the annotation 'stringKeys' 'booleanKeys' and 'integerKeys'
 * into a common reusable structure.
 */
data class GsonSubTypeKeyAndClass(val key: String, val clazzTypeMirror: TypeMirror)

/**
 * The type of key used when determining the correct subtype
 */
enum class SubTypeKeyType {
    STRING, INTEGER, BOOLEAN
}