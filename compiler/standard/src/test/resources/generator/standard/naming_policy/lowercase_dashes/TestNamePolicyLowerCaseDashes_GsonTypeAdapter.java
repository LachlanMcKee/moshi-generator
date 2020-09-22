package generator.standard.naming_policy.lowercase_dashes;

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
public final class TestNamePolicyLowerCaseDashes_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyLowerCaseDashes> {
    public TestNamePolicyLowerCaseDashes_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNamePolicyLowerCaseDashes readImpl(JsonReader reader) throws IOException {
        TestNamePolicyLowerCaseDashes result = new TestNamePolicyLowerCaseDashes();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "test-value":
                    Integer value_test_value = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_test_value != null) {
                        result.testValue = value_test_value;
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
    public void writeImpl(JsonWriter writer, TestNamePolicyLowerCaseDashes value) throws
            IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.testValue;
        writer.name("test-value");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
