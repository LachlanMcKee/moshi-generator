package gsonpath.dependencies

import gsonpath.adapter.standard.adapter.AdapterModelMetadataFactory
import gsonpath.adapter.standard.adapter.StandardGsonAdapterGenerator
import gsonpath.adapter.standard.adapter.read.ReadFunctions
import gsonpath.adapter.standard.adapter.write.WriteFunctions
import gsonpath.adapter.standard.extension.ExtensionsHandler
import gsonpath.adapter.standard.extension.ExtensionsLoader
import gsonpath.adapter.standard.extension.subtype.SubTypeMetadataFactoryImpl
import gsonpath.adapter.standard.factory.TypeAdapterFactoryGenerator
import gsonpath.adapter.standard.interf.InterfaceModelMetadataFactory
import gsonpath.adapter.standard.interf.ModelInterfaceGenerator
import gsonpath.adapter.standard.model.*
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
                FieldPathFetcher(FieldNamingPolicyMapper()))
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
                standardGsonAdapterGenerator = StandardGsonAdapterGenerator(
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