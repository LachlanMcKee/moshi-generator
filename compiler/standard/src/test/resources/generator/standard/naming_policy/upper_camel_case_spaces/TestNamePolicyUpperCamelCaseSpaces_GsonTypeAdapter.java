package generator.standard.naming_policy.upper_camel_case_spaces;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyUpperCamelCaseSpaces> {
    public TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNamePolicyUpperCamelCaseSpaces readImpl(JsonReader in) throws IOException {
        TestNamePolicyUpperCamelCaseSpaces result = new TestNamePolicyUpperCamelCaseSpaces();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Test Value":
                    Integer value_Test_Value = moshi.getAdapter(Integer.class).read(in);
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
    public void writeImpl(JsonWriter out, TestNamePolicyUpperCamelCaseSpaces value) throws
            IOException {
        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("Test Value");
        moshi.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}
