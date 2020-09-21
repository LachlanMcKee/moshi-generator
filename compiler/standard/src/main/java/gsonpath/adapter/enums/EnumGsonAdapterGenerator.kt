package gsonpath.adapter.enums

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.moshi.Moshi
import gsonpath.LazyFactoryMetadata
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.standard.adapter.properties.PropertyFetcher
import gsonpath.adapter.util.writeFile
import gsonpath.annotation.EnumGsonAdapter
import gsonpath.audit.AuditLog
import gsonpath.compiler.generateClassName
import gsonpath.internal.GsonPathTypeAdapter
import gsonpath.internal.GsonUtil
import gsonpath.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class EnumGsonAdapterGenerator(
        private val fileWriter: FileWriter,
        private val enumAdapterPropertiesFactory: EnumAdapterPropertiesFactory
) {

    @Throws(ProcessingException::class)
    fun handle(
            enumElement: TypeElement,
            autoGsonAnnotation: EnumGsonAdapter,
            lazyFactoryMetadata: LazyFactoryMetadata): AdapterGenerationResult {

        val propertyFetcher = PropertyFetcher(enumElement)

        val fieldNamingPolicy = propertyFetcher.getProperty("fieldNamingPolicy",
                autoGsonAnnotation.fieldNamingPolicy,
                lazyFactoryMetadata.annotation.fieldNamingPolicy)

        val properties = enumAdapterPropertiesFactory.create(
                autoGsonAnnotation.ignoreDefaultValue, enumElement, fieldNamingPolicy)

        val typeName = properties.enumTypeName
        val adapterClassName = ClassName.get(typeName.packageName(), generateClassName(typeName, "GsonTypeAdapter"))

        createEnumAdapterSpec(adapterClassName, properties)
                .writeFile(fileWriter, adapterClassName.packageName()) {
                    it.addStaticImport(GsonUtil::class.java, "*")
                }
        return AdapterGenerationResult(arrayOf(typeName), adapterClassName)
    }

    private fun createEnumAdapterSpec(
            adapterClassName: ClassName,
            properties: EnumAdapterProperties) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        superclass(ParameterizedTypeName.get(ClassName.get(GsonPathTypeAdapter::class.java), properties.enumTypeName))
        addAnnotation(Constants.GENERATED_ANNOTATION)

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Moshi::class.java, "moshi")
            code {
                addStatement("super(moshi)")
            }
        }

        addMethod(createReadMethod(properties))
        addMethod(createWriteMethod(properties))
    }

    private fun createReadMethod(properties: EnumAdapterProperties): MethodSpec {
        val enumTypeName = properties.enumTypeName
        return AdapterMethodBuilder.createReadMethodBuilder(properties.enumTypeName).applyAndBuild {
            code {
                createVariable(String::class.java, "enumValue", "reader.nextString()")
                switch("enumValue") {
                    properties.fields.forEach { (enumValueTypeName, label) ->
                        case("\"$label\"", addBreak = false) {
                            `return`("\$T", enumValueTypeName)
                        }
                    }
                    default(addBreak = false) {
                        if (properties.defaultValue != null) {
                            createVariable(AuditLog::class.java, "auditLog", "\$T.fromReader(reader)", AuditLog::class.java)
                            `if`("auditLog != null") {
                                addStatement(
                                        "auditLog.addUnexpectedEnumValue(new \$T(\"${properties.enumTypeName}\", reader.getPath(), enumValue))",
                                        AuditLog.UnexpectedEnumValue::class.java)
                            }
                            `return`("\$T", properties.defaultValue.enumValueTypeName)
                        } else {
                            addEscapedStatement("""throw new gsonpath.exception.JsonUnexpectedEnumValueException(enumValue, "$enumTypeName")""")
                        }
                    }
                }
            }
        }
    }

    private fun createWriteMethod(properties: EnumAdapterProperties): MethodSpec {
        return AdapterMethodBuilder.createWriteMethodBuilder(properties.enumTypeName).applyAndBuild {
            code {
                switch("value") {
                    properties.fields.forEach { (enumValueTypeName, label) ->
                        case(enumValueTypeName.simpleName()) {
                            addStatement("writer.value(\"$label\")")
                        }
                    }
                }
            }
        }
    }
}
