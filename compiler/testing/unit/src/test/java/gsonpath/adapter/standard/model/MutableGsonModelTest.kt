package gsonpath.adapter.standard.model

import gsonpath.generator.processingExceptionMatcher
import gsonpath.model.FieldInfo
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock

class MutableGsonModelTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testGsonField() {
        val mutableGsonField = MutableGsonField(0, TestGsonValue("Foo"))

        Assert.assertEquals(
                GsonField(0, TestGsonValue("Foo")),
                mutableGsonField.toImmutable())
    }

    @Test
    fun testGsonObject() {
        val fieldInfo = mock(FieldInfo::class.java)
        val mutableGsonField = MutableGsonField(0, TestGsonValue("Foo"))

        val mutableGsonObject = MutableGsonObject<TestGsonValue>()
        mutableGsonObject.addField("field1", mutableGsonField)
        mutableGsonObject.addObject("object1", MutableGsonObject())
        val array = mutableGsonObject.addArray("array1")
        array.getObjectAtIndex(0)

        Assert.assertEquals(
                GsonObject(
                        mapOf(
                                "field1" to GsonField(0, TestGsonValue("Foo")),
                                "object1" to GsonObject(emptyMap()),
                                "array1" to GsonArray(mapOf(0 to GsonObject<TestGsonValue>(emptyMap())), 0)
                        )
                ),
                mutableGsonObject.toImmutable())
    }

    @Test
    fun testGsonArray() {
        val fieldInfo = mock(FieldInfo::class.java)
        val mutableGsonField = MutableGsonField(0, TestGsonValue("Foo"))
        val mutableGsonArray = MutableGsonArray<TestGsonValue>()

        mutableGsonArray.addField(0, mutableGsonField)
        mutableGsonArray.getObjectAtIndex(1)

        Assert.assertEquals(
                GsonArray(
                        mapOf(
                                0 to GsonField(0, TestGsonValue("Foo")),
                                1 to GsonObject<TestGsonValue>(emptyMap())
                        ),
                        1
                ),
                mutableGsonArray.toImmutable())
    }

    @Test
    fun testInvalidGsonArray() {
        expectedException.expect(`is`(processingExceptionMatcher(null, "Array should not be empty")))
        MutableGsonArray<TestGsonValue>().toImmutable()
    }
}