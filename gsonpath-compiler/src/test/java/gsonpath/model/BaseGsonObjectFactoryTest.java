package gsonpath.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.TypeName;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;
import gsonpath.ProcessingException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseGsonObjectFactoryTest {
    static final String DEFAULT_VARIABLE_NAME = "variableName";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    FieldInfo mockFieldInfo(String fieldName) {
        return mockFieldInfo(fieldName, null);
    }

    FieldInfo mockFieldInfo(String fieldName, String jsonPath) {
        FieldInfo fieldInfo = mock(FieldInfo.class);
        when(fieldInfo.getTypeName()).thenReturn(TypeName.INT);
        when(fieldInfo.getAnnotationNames()).thenReturn(new String[0]);
        when(fieldInfo.getFieldName()).thenReturn(fieldName);

        if (jsonPath != null) {
            SerializedName serializedName = mock(SerializedName.class);
            when(serializedName.value()).thenReturn(jsonPath);
            when(fieldInfo.getAnnotation(SerializedName.class)).thenReturn(serializedName);
        }

        return fieldInfo;
    }

    GsonObject executeAddGsonType(GsonTypeArguments arguments) throws ProcessingException {
        return executeAddGsonType(arguments, new GsonObject());
    }

    GsonObject executeAddGsonType(GsonTypeArguments arguments, GsonObject outputGsonObject) throws ProcessingException {
        new GsonObjectFactory().addGsonType(
                outputGsonObject,
                arguments.fieldInfo,
                arguments.fieldInfoIndex,
                arguments.flattenDelimiter,
                arguments.gsonFieldNamingPolicy,
                arguments.gsonFieldValidationType,
                arguments.pathSubstitutions
        );

        return outputGsonObject;
    }

    static class GsonTypeArguments {
        private FieldInfo fieldInfo;
        private int fieldInfoIndex = 0;
        private char flattenDelimiter = '.';
        private FieldNamingPolicy gsonFieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        private GsonFieldValidationType gsonFieldValidationType = GsonFieldValidationType.NO_VALIDATION;
        private PathSubstitution[] pathSubstitutions = new PathSubstitution[0];

        public GsonTypeArguments(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
        }

        public GsonTypeArguments fieldInfoIndex(int fieldInfoIndex) {
            this.fieldInfoIndex = fieldInfoIndex;
            return this;
        }

        public GsonTypeArguments flattenDelimiter(char flattenDelimiter) {
            this.flattenDelimiter = flattenDelimiter;
            return this;
        }

        public GsonTypeArguments gsonFieldNamingPolicy(FieldNamingPolicy gsonFieldNamingPolicy) {
            this.gsonFieldNamingPolicy = gsonFieldNamingPolicy;
            return this;
        }

        public GsonTypeArguments gsonFieldValidationType(GsonFieldValidationType gsonFieldValidationType) {
            this.gsonFieldValidationType = gsonFieldValidationType;
            return this;
        }

        public GsonTypeArguments pathSubstitutions(PathSubstitution... pathSubstitutions) {
            this.pathSubstitutions = pathSubstitutions;
            return this;
        }
    }
}
