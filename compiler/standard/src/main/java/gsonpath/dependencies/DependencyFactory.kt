package gsonpath.dependencies

import gsonpath.extension.ExtensionsHandler
import gsonpath.extension.ExtensionsLoader
import gsonpath.generator.standard.adapter.AdapterModelMetadataFactory
import gsonpath.generator.standard.adapter.AutoGsonAdapterGenerator
import gsonpath.generator.standard.adapter.read.ReadFunctions
import gsonpath.generator.standard.adapter.write.WriteFunctions
import gsonpath.generator.standard.extension.subtype.SubTypeMetadataFactoryImpl
import gsonpath.generator.standard.factory.TypeAdapterFactoryGenerator
import gsonpath.generator.standard.interf.InterfaceModelMetadataFactory
import gsonpath.generator.standard.interf.ModelInterfaceGenerator
import gsonpath.model.*
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

object DependencyFactory {

    fun create(processingEnv: ProcessingEnvironment): Dependencies {

        val fileWriter = FileWriter(processingEnv)
        val defaultValueDetector = DefaultValueDetectorImpl(processingEnv)

        val typeHandler = ProcessorTypeHandler(processingEnv)
        val fieldGetterFinder = FieldGetterFinder(typeHandler)
        val annotationFetcher = AnnotationFetcher(typeHandler, fieldGetterFinder)
        val gsonObjectFactory = GsonObjectFactory(
                GsonObjectValidator(),
                FieldPathFetcher(SerializedNameFetcher, FieldNamingPolicyMapper()))
        val gsonObjectTreeFactory = GsonObjectTreeFactory(gsonObjectFactory)

        val extensions = ExtensionsLoader.loadExtensions(typeHandler, Logger(processingEnv))
        val extensionsHandler = ExtensionsHandler(processingEnv, extensions)
        val readFunctions = ReadFunctions(extensionsHandler)
        val writeFunctions = WriteFunctions(extensionsHandler)
        val modelInterfaceGenerator = ModelInterfaceGenerator(InterfaceModelMetadataFactory(typeHandler), fileWriter)
        val adapterModelMetadataFactory = AdapterModelMetadataFactory(
                FieldInfoFactory(
                        typeHandler,
                        FieldTypeFactory(typeHandler),
                        fieldGetterFinder,
                        annotationFetcher,
                        defaultValueDetector),
                gsonObjectTreeFactory,
                typeHandler,
                modelInterfaceGenerator
        )

        // Handle the standard type adapters.
        return Dependencies(
                autoGsonAdapterGenerator = AutoGsonAdapterGenerator(
                        adapterModelMetadataFactory,
                        fileWriter,
                        readFunctions,
                        writeFunctions),
                fileWriter = fileWriter,
                typeAdapterFactoryGenerator = TypeAdapterFactoryGenerator(
                        fileWriter),
                subTypeMetadataFactory = SubTypeMetadataFactoryImpl(typeHandler)
        )
    }
}