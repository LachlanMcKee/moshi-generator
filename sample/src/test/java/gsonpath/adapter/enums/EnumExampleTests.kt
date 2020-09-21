package gsonpath.adapter.enums

import com.squareup.moshi.JsonReader
import gsonpath.TestUtil.createMoshi
import gsonpath.audit.AuditLog
import junit.framework.TestCase.assertEquals
import okio.Okio
import org.junit.Test

class EnumExampleTests {

    @Test
    fun testEnum() {
        val jsonReader = JsonReader.of(Okio.buffer(Okio.source("""
            {
              "values": [
                "value1",
                "value-2",
                "value-3-and-4"
              ],
              "valuesWithDefault": ["UNEXPECTED"]
            }
        """.trimIndent().byteInputStream())))

        val result = createMoshi()
                .adapter(EnumExample::class.java)
                .fromJson(jsonReader)!!

        assertEquals(3, result.values.size)
        assertEquals(EnumExample.EnumValue.VALUE1, result.values[0])
        assertEquals(EnumExample.EnumValue.VALUE_2, result.values[1])
        assertEquals(EnumExample.EnumValue.VALUE_3_AND_4, result.values[2])

        assertEquals(1, result.valuesWithDefault.size)
        assertEquals(EnumExample.EnumValueWithDefault.VALUE1, result.valuesWithDefault[0])

        val unexpectedEnumValues = AuditLog.fromReader(jsonReader).unexpectedEnumValues
        assertEquals(1, unexpectedEnumValues.size)
        assertEquals("\$.valuesWithDefault[1]", unexpectedEnumValues[0].path)
        assertEquals("gsonpath.adapter.enums.EnumExample.EnumValueWithDefault", unexpectedEnumValues[0].typeName)
        assertEquals("UNEXPECTED", unexpectedEnumValues[0].value)
    }

}
