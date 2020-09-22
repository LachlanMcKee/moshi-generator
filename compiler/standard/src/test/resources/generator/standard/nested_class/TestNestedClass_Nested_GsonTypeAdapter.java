package generator.standard.nested_class;

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
public final class TestNestedClass_Nested_GsonTypeAdapter extends GsonPathTypeAdapter<TestNestedClass.Nested> {
    public TestNestedClass_Nested_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNestedClass.Nested readImpl(JsonReader reader) throws IOException {
        TestNestedClass.Nested result = new TestNestedClass.Nested();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    Integer value_value1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
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
    public void writeImpl(JsonWriter writer, TestNestedClass.Nested value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("value1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
