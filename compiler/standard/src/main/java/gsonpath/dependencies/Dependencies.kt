package gsonpath.dependencies

import gsonpath.generator.standard.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.standard.extension.subtype.SubTypeMetadataFactory
import gsonpath.generator.standard.factory.TypeAdapterFactoryGenerator
import gsonpath.util.FileWriter

data class Dependencies(
        val autoGsonAdapterGenerator: AutoGsonAdapterGenerator,
        val fileWriter: FileWriter,
        val typeAdapterFactoryGenerator: TypeAdapterFactoryGenerator,
        val subTypeMetadataFactory: SubTypeMetadataFactory)