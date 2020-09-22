package generator.standard.naming_policy.upper_camel_case_spaces;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyUpperCamelCaseSpaces> {
    public TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNamePolicyUpperCamelCaseSpaces readImpl(JsonReader reader) throws IOException {
        TestNamePolicyUpperCamelCaseSpaces result = new TestNamePolicyUpperCamelCaseSpaces();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "Test Value":
                    Integer value_Test_Value = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_Test_Value != null) {
                        result.testValue = value_Test_Value;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestNamePolicyUpperCamelCaseSpaces value) throws
            IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.testValue;
        writer.name("Test Value");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
