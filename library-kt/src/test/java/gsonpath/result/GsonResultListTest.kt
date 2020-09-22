package gsonpath.result

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import gsonpath.GsonPathTypeAdapterFactoryKt
import gsonpath.GsonResult
import gsonpath.GsonResultList
import okio.Okio
import org.hamcrest.CustomTypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test

class GsonResultListTest {
    @Test
    fun testUsingGsonSafeArrayList() {
        val moshi = Moshi.Builder()
                .add(GsonPathTypeAdapterFactoryKt())
                .build()

        val resourceAsStream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream("sample.json")

        val nullableInt: Int? = 1

        val typesList = moshi.adapter<GsonResultList<Int>>(Types.newParameterizedType(GsonResultList::class.java, nullableInt!!::class.java))
                .fromJson(Okio.buffer(Okio.source(resourceAsStream)))!!

        assertEquals(4, typesList.size)
        assertEquals(GsonResult.Success(1), typesList[0])
        assertThat(typesList[1], createFailureMatcher("Expected NUMBER but was a, a java.lang.String, at path \$"))
        assertThat(typesList[2], createFailureMatcher("Expected NUMBER but was b, a java.lang.String, at path \$"))
        assertEquals(GsonResult.Success(4), typesList[3])
    }

    private fun createFailureMatcher(message: String) = object : CustomTypeSafeMatcher<GsonResult<Int>>("") {
        override fun matchesSafely(item: GsonResult<Int>): Boolean {
            return (item as GsonResult.Failure).exception.let {
                it is JsonDataException && it.message == message
            }
        }
    }
}
