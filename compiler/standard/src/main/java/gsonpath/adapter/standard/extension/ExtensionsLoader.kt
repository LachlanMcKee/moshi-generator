package gsonpath.adapter.standard.extension

import gsonpath.ProcessingException
import gsonpath.adapter.standard.extension.def.intdef.IntDefExtension
import gsonpath.adapter.standard.extension.def.stringdef.StringDefExtension
import gsonpath.adapter.standard.extension.empty.EmptyToNullExtension
import gsonpath.adapter.standard.extension.flatten.FlattenJsonExtension
import gsonpath.adapter.standard.extension.invalid.RemoveInvalidElementsExtension
import gsonpath.adapter.standard.extension.range.floatrange.FloatRangeExtension
import gsonpath.adapter.standard.extension.range.intrange.IntRangeExtension
import gsonpath.adapter.standard.extension.size.SizeExtension
import gsonpath.adapter.standard.extension.subtype.GsonSubTypeExtension
import gsonpath.adapter.standard.extension.subtype.SubTypeMetadataFactoryImpl
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.Logger
import gsonpath.util.TypeHandler
import java.util.*

object ExtensionsLoader {

    fun loadExtensions(typeHandler: TypeHandler, logger: Logger): List<GsonPathExtension> {
        // Load any extensions that are also available at compile time.
        println()
        val extensions: List<GsonPathExtension> =
                try {
                    ServiceLoader.load(GsonPathExtension::class.java, javaClass.classLoader).toList()

                } catch (t: Throwable) {
                    throw ProcessingException("Failed to load one or more GsonPath extensions. Cause: ${t.message}")
                }

        // Print the extensions for auditing purposes.
        extensions.forEach {
            logger.printMessage("Extension found: " + it.extensionName)
        }

        return extensions.plus(arrayOf(
                IntDefExtension(),
                StringDefExtension(),
                EmptyToNullExtension(),
                FlattenJsonExtension(),
                RemoveInvalidElementsExtension(),
                FloatRangeExtension(),
                IntRangeExtension(),
                SizeExtension(),
                GsonSubTypeExtension(typeHandler, SubTypeMetadataFactoryImpl(typeHandler))
        ))
    }
}