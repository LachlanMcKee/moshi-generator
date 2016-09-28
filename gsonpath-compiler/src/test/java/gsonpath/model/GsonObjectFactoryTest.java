package gsonpath.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;
import gsonpath.ProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class GsonObjectFactoryTest {

    public static class StandardTests extends BaseGsonObjectFactoryTest {
        @Test
        public void givenNoJsonPathAnnotation_whenAddGsonType_expectSingleGsonObject() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, new GsonField(0, fieldInfo, DEFAULT_VARIABLE_NAME, false));
            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }

        @Test
        public void givenJsonPath_whenAddGsonType_expectMultipleGsonObjects() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.child");

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            GsonObject gsonObject = new GsonObject();
            gsonObject.addField("child", new GsonField(0, fieldInfo, "root.child", false));
            expectedGsonObject.addObject("root", gsonObject);

            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }

        @Test
        public void givenJsonPathWithDanglingDelimiter_whenAddGsonType_expectMultipleGsonObjects() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.");

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            GsonObject gsonObject = new GsonObject();
            gsonObject.addField(DEFAULT_VARIABLE_NAME, new GsonField(0, fieldInfo, "root." + DEFAULT_VARIABLE_NAME, false));
            expectedGsonObject.addObject("root", gsonObject);

            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }

        @Test
        public void givenPathSubstitution_whenAddGsonType_expectReplacedJsonPath() throws ProcessingException {
            // given
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "{REPLACE_ME}.value");
            PathSubstitution pathSubstitution = mock(PathSubstitution.class);
            when(pathSubstitution.original()).thenReturn("REPLACE_ME");
            when(pathSubstitution.replacement()).thenReturn("replacement");

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo)
                    .pathSubstitutions(pathSubstitution));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            expectedGsonObject.addObject("replacement", new GsonObject())
                    .addField("value", new GsonField(0, fieldInfo, "replacement.value", false));

            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }

        @Test
        public void givenObjectType_whenAddGsonType_throwInvalidFieldTypeException() throws ProcessingException {
            // given
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);
            when(fieldInfo.getTypeName()).thenReturn(TypeName.OBJECT);

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Invalid field type: java.lang.Object");
            executeAddGsonType(new GsonTypeArguments(fieldInfo));
        }

        @Test
        public void givenBothNonNullAndNullableAnnotations_whenAddGsonType_throwIncorrectAnnotationsException() throws ProcessingException {
            // given
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);
            when(fieldInfo.getTypeName()).thenReturn(TypeName.INT.box());
            when(fieldInfo.getAnnotationNames()).thenReturn(new String[]{"NonNull", "Nullable"});

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Field cannot have both Mandatory and Optional annotations");
            executeAddGsonType(new GsonTypeArguments(fieldInfo));
        }

        @Test
        public void givenDuplicateChildFields_whenAddGsonType_throwDuplicateFieldException() throws ProcessingException {
            // given
            GsonObject existingGsonObject = new GsonObject();
            GsonField existingField = new GsonField(0, mockFieldInfo(DEFAULT_VARIABLE_NAME), DEFAULT_VARIABLE_NAME, false);
            existingGsonObject.addField(DEFAULT_VARIABLE_NAME, existingField);

            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Unexpected duplicate field 'variableName' found. Each tree branch must use a unique value!");
            executeAddGsonType(new GsonTypeArguments(fieldInfo), existingGsonObject);
        }

        @Test
        public void givenExistingObjectField_whenAddGsonType_throwDuplicateFieldException() throws ProcessingException {
            // given
            String duplicateBranchName = "duplicate";

            GsonObject existingGsonObject = new GsonObject();
            GsonField existingField = new GsonField(0, mockFieldInfo(duplicateBranchName), duplicateBranchName, false);
            existingGsonObject.addField(duplicateBranchName, existingField);

            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, duplicateBranchName + "." + DEFAULT_VARIABLE_NAME);

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!");
            executeAddGsonType(new GsonTypeArguments(fieldInfo), existingGsonObject);
        }

        @Test
        public void givenExistingObjectField_whenAddNestedField_throwDuplicateFieldException() throws ProcessingException {
            // given
            String duplicateBranchName = "duplicate";

            GsonObject existingGsonObject = new GsonObject();
            GsonField existingField = new GsonField(0, mockFieldInfo(duplicateBranchName), duplicateBranchName, false);

            GsonObject childObject = new GsonObject();
            childObject.addField(duplicateBranchName, existingField);
            existingGsonObject.addObject(DEFAULT_VARIABLE_NAME, childObject);

            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, DEFAULT_VARIABLE_NAME + "." + duplicateBranchName);

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!");
            executeAddGsonType(new GsonTypeArguments(fieldInfo), existingGsonObject);
        }
    }

    @RunWith(Parameterized.class)
    public static class RequiredAnnotationsTest extends BaseGsonObjectFactoryTest {
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    // Test 'NonNull' annotation permutations with a non-primitive type
                    {"NonNull", GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false},
                    {"NonNull", GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true},
                    {"NonNull", GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), true},

                    // Test 'Nullable' annotation permutations with a non-primitive type
                    {"Nullable", GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false},
                    {"Nullable", GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), false},
                    {"Nullable", GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false},

                    // Test no annotation permutations with a non-primitive type
                    {null, GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false},
                    {null, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true},
                    {null, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false},

                    // Test no annotation permutations with a primitive type
                    {null, GsonFieldValidationType.NO_VALIDATION, TypeName.INT, false},
                    {null, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT, true},
                    {null, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT, true},
            });
        }

        private final String requiredTypeAnnotation;
        private final GsonFieldValidationType gsonFieldValidationType;
        private final TypeName fieldTypeName;
        private final boolean isRequired;

        public RequiredAnnotationsTest(String requiredTypeAnnotation, GsonFieldValidationType gsonFieldValidationType, TypeName fieldTypeName, boolean isRequired) {
            this.requiredTypeAnnotation = requiredTypeAnnotation;
            this.gsonFieldValidationType = gsonFieldValidationType;
            this.fieldTypeName = fieldTypeName;
            this.isRequired = isRequired;
        }

        @Test
        public void test() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);
            when(fieldInfo.getTypeName()).thenReturn(fieldTypeName);

            if (requiredTypeAnnotation != null) {
                when(fieldInfo.getAnnotationNames()).thenReturn(new String[]{requiredTypeAnnotation});
            }

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo)
                    .gsonFieldValidationType(gsonFieldValidationType));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, new GsonField(0, fieldInfo, DEFAULT_VARIABLE_NAME, isRequired));
            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }
    }

    @RunWith(Parameterized.class)
    public static class MandatoryAnnotationsTest extends BaseGsonObjectFactoryTest {
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"NonNull"},
                    {"Nonnull"},
                    {"NotNull"},
                    {"Notnull"}
            });
        }

        private final String mandatoryAnnotation;

        public MandatoryAnnotationsTest(String mandatoryAnnotation) {
            this.mandatoryAnnotation = mandatoryAnnotation;
        }

        @Test
        public void givenPrimitiveField_whenAddGsonType_throwProcessingException() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);
            when(fieldInfo.getAnnotationNames()).thenReturn(new String[]{mandatoryAnnotation});

            // when / then
            exception.expect(ProcessingException.class);
            exception.expectMessage("Primitives should not use NonNull or Nullable annotations");
            executeAddGsonType(new GsonTypeArguments(fieldInfo));
        }

        @Test
        public void givenNonPrimitiveFieldAndValidateNonNull_whenAddGsonType_expectIsRequired() throws ProcessingException {
            // when
            FieldInfo fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME);
            when(fieldInfo.getTypeName()).thenReturn(ClassName.INT.box());
            when(fieldInfo.getAnnotationNames()).thenReturn(new String[]{mandatoryAnnotation});

            // when
            GsonObject outputGsonObject = executeAddGsonType(new GsonTypeArguments(fieldInfo)
                    .gsonFieldValidationType(GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL));

            // then
            GsonObject expectedGsonObject = new GsonObject();
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, new GsonField(0, fieldInfo, DEFAULT_VARIABLE_NAME, true));
            Assert.assertEquals(expectedGsonObject, outputGsonObject);
        }
    }
}
