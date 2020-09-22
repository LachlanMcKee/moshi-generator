package generator.standard.naming_policy.identity;

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
public final class TestNamePolicyIdentity_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyIdentity> {
    public TestNamePolicyIdentity_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNamePolicyIdentity readImpl(JsonReader reader) throws IOException {
        TestNamePolicyIdentity result = new TestNamePolicyIdentity();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "testValue":
                    Integer value_testValue = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_testValue != null) {
                        result.testValue = value_testValue;
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
    public void writeImpl(JsonWriter writer, TestNamePolicyIdentity value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.testValue;
        writer.name("testValue");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
