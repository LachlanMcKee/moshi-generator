package gsonpath.generator.standard

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock
import javax.lang.model.element.Element
import org.mockito.Mockito.`when` as whenever

class SharedFunctionsTest {
    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun givenInvalidFieldType_whenTestValidateFieldAnnotations_expectException() {
        createFieldInfo(typeName = TypeName.CHAR).apply {
            whenever(element).thenReturn(mock(Element::class.java))

            exception.expect(`is`(processingExceptionMatcher(element)))
            SharedFunctions.validateFieldAnnotations(this)
        }
    }

    @Test
    fun givenValidFlattenJsonFieldType_whenTestValidateFieldAnnotations_expectNoException() {
        SharedFunctions.validateFieldAnnotations(createFieldInfo())
    }

    @Test
    fun givenNoFlattenJsonFieldType_whenTestValidateFieldAnnotations_expectNoException() {
        SharedFunctions.validateFieldAnnotations(createFieldInfo(hasFlattenJson = false))
    }

    private fun createFieldInfo(
            hasFlattenJson: Boolean = true,
            typeName: TypeName = ClassName.get(String::class.java)): FieldInfo {

        return mock(FieldInfo::class.java).apply {
            if (hasFlattenJson) {
                whenever(getAnnotation(FlattenJson::class.java)).thenReturn(mock(FlattenJson::class.java))
            }
            whenever(this.typeName).thenReturn(typeName)
        }
    }

    private fun processingExceptionMatcher(element: Element): TypeSafeMatcher<ProcessingException> {
        return object : TypeSafeMatcher<ProcessingException>() {
            override fun describeTo(description: Description) {
            }

            override fun matchesSafely(item: ProcessingException): Boolean {
                return item.element == element && item.message == "FlattenObject can only be used on String variables"
            }
        }
    }
}
