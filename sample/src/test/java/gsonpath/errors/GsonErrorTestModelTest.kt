package gsonpath.errors

import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import gsonpath.GsonPath
import gsonpath.GsonPathTypeAdapterFactory
import gsonpath.TestGsonTypeFactory
import gsonpath.audit.AuditLog
import gsonpath.audit.AuditLog.RemovedElement
import okio.Okio
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class GsonErrorTestModelTest {
    @Test
    @Throws(IOException::class)
    fun test() {
        val moshi = Moshi.Builder()
                .add(GsonPathTypeAdapterFactory())
                .add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory::class.java))
                .build()

        val reader = JsonReader.of(Okio.buffer(Okio.source(ClassLoader.getSystemClassLoader().getResourceAsStream("GsonErrorTestJson.json")!!)))

        moshi.adapter(GsonErrorTestModel::class.java).fromJson(reader)

        val removedListElements = reader.tag(AuditLog::class.java)!!.removedElements

        assertEquals(6, removedListElements.size)

        assertRemovedElement(removedListElements, 0,
                "$.singleValue.values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                mapOf("text" to null))

        assertRemovedElement(removedListElements, 1,
                "$.listValue[0].values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                mapOf("text" to null))

        assertRemovedElement(removedListElements, 2,
                "$.listValue[1].values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                mapOf("text" to null))

        assertRemovedElement(removedListElements, 3,
                "$.arrayValue[0].values",
                "Invalid string length() for JSON element 'text'. Expected minimum: '1', actual minimum: '0'",
                mapOf("text" to ""))

        assertRemovedElement(removedListElements, 4,
                "$.arrayValue[1].values",
                "Invalid string length() for JSON element 'text'. Expected maximum: '5', actual maximum: '6'",
                mapOf("text" to "123456"))

        assertRemovedElement(removedListElements, 5,
                "$.mapValue.key1.values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                mapOf("text" to null))
    }

    private fun assertRemovedElement(
            errors: List<RemovedElement>, index: Int, path: String, exceptionMessage: String, jsonElement: Any) {
        val error = errors[index]
        assertEquals(path, error.path)
        assertEquals(exceptionMessage, error.exception.message)
        assertEquals(jsonElement, error.jsonElement)
    }
}
