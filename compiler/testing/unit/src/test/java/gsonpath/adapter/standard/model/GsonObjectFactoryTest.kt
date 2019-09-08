package gsonpath.adapter.standard.model

import gsonpath.ProcessingException
import gsonpath.adapter.AdapterFieldMetadata
import gsonpath.model.FieldInfoTestFactory.mockFieldInfo
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.Mockito.`when` as whenever

@RunWith(Enclosed::class)
class GsonObjectFactoryTest {

    class StandardTests : BaseGsonObjectFactoryTest() {
        @Test
        @Throws(ProcessingException::class)
        fun givenNoJsonPathAnnotation_whenAddGsonType_expectSingleGsonObject() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME)
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, MutableGsonField(0, AdapterFieldMetadata(fieldInfo, DEFAULT_VARIABLE_NAME_2, DEFAULT_VARIABLE_NAME, false)))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPath_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.child")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.child"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val mutableGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            mutableGsonObject.addField("child", MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_root_child", "root.child", false)))
            expectedGsonObject.addObject("root", mutableGsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathWithDanglingDelimiter_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.variableName"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val mutableGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            mutableGsonObject.addField(DEFAULT_VARIABLE_NAME, MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_root_$DEFAULT_VARIABLE_NAME", "root.$DEFAULT_VARIABLE_NAME", false)))
            expectedGsonObject.addObject("root", mutableGsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenPathSubstitution_whenAddGsonType_expectReplacedJsonPath() {
            // given
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "{REPLACE_ME_1}.{REPLACE_ME_2}")

            val metadata = createMetadata()
            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("replacement.value"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            expectedGsonObject.addObject("replacement", MutableGsonObject<AdapterFieldMetadata>())
                    .addField("value", MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_replacement_value", "replacement.value", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathArray_whenAddGsonType_expectRootArray() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5]")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard("element[5]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val elementArray = expectedGsonObject.addArray("element")
            elementArray.addField(5, MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_element_5_", "element[5]", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathArrayNestedWithinObject_whenAddGsonType_expectArrayNestedWithinObject() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "value.element[5]")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("value.element[5]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val valueObject = MutableGsonObject<AdapterFieldMetadata>()
            expectedGsonObject.addObject("value", valueObject)
            val elementArray = valueObject.addArray("element")
            elementArray.addField(5, MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_value_element_5_", "value.element[5]", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenObjectNestedWithinJsonPathArray_whenAddGsonType_expectObjectNestedWithinArray() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5].value")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element[5].value"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val elementArray = expectedGsonObject.addArray("element")
            val elementItemObject = elementArray.getObjectAtIndex(5)
            elementItemObject.addField("value", MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_element_5__value", "element[5].value", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenMultipleJsonPathArrays_whenAddGsonType_expectMultipleArrays() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5].value[0]")
            val metadata = createMetadata()

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element[5].value[0]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val elementArray = expectedGsonObject.addArray("element")
            val elementItemObject = elementArray.getObjectAtIndex(5)
            val valueArray = elementItemObject.addArray("value")
            valueArray.addField(0, MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_element_5__value_0_", "element[5].value[0]", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenMultipleJsonPathArrays_whenAddGsonType_expectMultipleArrays2() {
            // when
            val metadata = createMetadata()

            val defGsonFieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element.value[5].test.def")

            whenever(fieldInfoPathFetcher.getJsonFieldPath(defGsonFieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element.value[5].test.def"))

            val previousGsonObject = executeAddGsonType(GsonTypeArguments(defGsonFieldInfo), metadata)
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element.value[5].test.abc")

            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element.value[5].test.abc"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, previousGsonObject)

            // then
            val expectedGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val elementObject = expectedGsonObject.addObject("element", MutableGsonObject<AdapterFieldMetadata>())
            val valueArray = elementObject.addArray("value")
            val testObject = valueArray.getObjectAtIndex(5)
            val abcObject = testObject.addObject("test", MutableGsonObject<AdapterFieldMetadata>())

            abcObject.addField("def", MutableGsonField(0, AdapterFieldMetadata(defGsonFieldInfo, "value_element_value_5__test_def", "element.value[5].test.def", false)))
            abcObject.addField("abc", MutableGsonField(0, AdapterFieldMetadata(fieldInfo, "value_element_value_5__test_abc", "element.value[5].test.abc", false)))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenDuplicateChildFields_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val existingGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val existingField = MutableGsonField(0, AdapterFieldMetadata(mockFieldInfo(DEFAULT_VARIABLE_NAME), DEFAULT_VARIABLE_NAME_2, DEFAULT_VARIABLE_NAME, false))
            existingGsonObject.addField(DEFAULT_VARIABLE_NAME, existingField)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME)

            val metadata = createMetadata()
            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'variableName' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val existingField = MutableGsonField(0, AdapterFieldMetadata(mockFieldInfo(duplicateBranchName), "value_$duplicateBranchName", duplicateBranchName, false))
            existingGsonObject.addField(duplicateBranchName, existingField)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "$duplicateBranchName.$DEFAULT_VARIABLE_NAME")

            val metadata = createMetadata()
            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("$duplicateBranchName.$DEFAULT_VARIABLE_NAME"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddNestedField_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = MutableGsonObject<AdapterFieldMetadata>()
            val existingField = MutableGsonField(0, AdapterFieldMetadata(mockFieldInfo(duplicateBranchName), "value_$duplicateBranchName", duplicateBranchName, false))

            val childObject = MutableGsonObject<AdapterFieldMetadata>()
            childObject.addField(duplicateBranchName, existingField)
            existingGsonObject.addObject(DEFAULT_VARIABLE_NAME, childObject)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "$DEFAULT_VARIABLE_NAME.$duplicateBranchName")

            val metadata = createMetadata()
            whenever(fieldInfoPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("$DEFAULT_VARIABLE_NAME.$duplicateBranchName"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }
    }

}
