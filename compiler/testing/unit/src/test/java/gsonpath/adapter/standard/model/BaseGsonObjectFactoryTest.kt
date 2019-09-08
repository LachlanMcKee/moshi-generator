package gsonpath.adapter.standard.model

import com.google.gson.FieldNamingPolicy
import com.nhaarman.mockitokotlin2.mock
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterFieldMetadata
import gsonpath.model.FieldInfo
import gsonpath.model.AdapterFieldInfo
import org.junit.Rule
import org.junit.rules.ExpectedException

open class BaseGsonObjectFactoryTest {

    @JvmField
    @Rule
    val exception: ExpectedException = ExpectedException.none()

    val fieldInfoRequiredDetector: FieldInfoRequiredDetector<FieldInfo> = mock()
    val fieldInfoPathFetcher: FieldInfoPathFetcher<FieldInfo> = mock()
    val gsonFieldValueFactory = AdapterGsonFieldValueFactory()

    @Throws(ProcessingException::class)
    fun executeAddGsonType(arguments: GsonTypeArguments, metadata: GsonObjectMetadata, outputGsonObject: MutableGsonObject<AdapterFieldMetadata> = MutableGsonObject()): MutableGsonObject<AdapterFieldMetadata> {
        GsonObjectFactory(fieldInfoRequiredDetector, fieldInfoPathFetcher, gsonFieldValueFactory).addGsonType(
                outputGsonObject,
                arguments.fieldInfo,
                arguments.fieldInfoIndex,
                metadata
        )

        return outputGsonObject
    }

    fun createMetadata(flattenDelimiter: Char = '.',
                       gsonFieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
                       gsonFieldValidationType: GsonFieldValidationType = GsonFieldValidationType.NO_VALIDATION,
                       pathSubstitutions: Array<PathSubstitution> = emptyArray()): GsonObjectMetadata {

        return GsonObjectMetadata(
                flattenDelimiter,
                gsonFieldNamingPolicy,
                gsonFieldValidationType,
                pathSubstitutions)
    }

    class GsonTypeArguments(
            val fieldInfo: AdapterFieldInfo,
            val fieldInfoIndex: Int = 0)

    companion object {
        const val DEFAULT_VARIABLE_NAME_2 = "value_variableName"
        const val DEFAULT_VARIABLE_NAME = "variableName"
    }
}
