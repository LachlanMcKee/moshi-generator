package generator.standard.naming_policy.upper_camel_case;

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
public final class TestNamePolicyUpperCamelCase_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyUpperCamelCase> {
    public TestNamePolicyUpperCamelCase_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNamePolicyUpperCamelCase readImpl(JsonReader reader) throws IOException {
        TestNamePolicyUpperCamelCase result = new TestNamePolicyUpperCamelCase();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "TestValue":
                    Integer value_TestValue = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_TestValue != null) {
                        result.testValue = value_TestValue;
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
    public void writeImpl(JsonWriter writer, TestNamePolicyUpperCamelCase value) throws
            IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.testValue;
        writer.name("TestValue");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
