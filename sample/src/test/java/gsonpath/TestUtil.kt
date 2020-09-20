package gsonpath

import com.squareup.moshi.Moshi
import org.junit.Assert

object TestUtil {

    fun createMoshi(): Moshi = Moshi.Builder()
            .add(GsonPathTypeAdapterFactory())
            .add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory::class.java))
            .build()

    fun <T> executeFromJson(clazz: Class<T>, jsonString: String): T = createMoshi()
            .adapter(clazz)
            .fromJson(jsonString)!!

    fun expectException(clazz: Class<*>, jsonString: String, message: String) {
        val exception: Exception? =
                try {
                    executeFromJson(clazz, jsonString)
                    null

                } catch (e: Exception) {
                    e
                }

        if (exception != null) {
            Assert.assertEquals(message, exception.message)
            return
        }

        Assert.fail("Expected exception was not thrown.")
    }
}
