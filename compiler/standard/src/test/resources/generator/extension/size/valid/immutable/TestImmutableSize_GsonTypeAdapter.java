package generator.standard.size.valid.nullable;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestImmutableSize_GsonTypeAdapter extends GsonPathTypeAdapter<TestImmutableSize> {
    public TestImmutableSize_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestImmutableSize readImpl(JsonReader reader) throws IOException {
        String[] value_value1 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    value_value1 = moshi.adapter(String[].class).fromJson(reader);

                    // Gsonpath Extensions
                    if (value_value1 != null) {

                        // Extension - 'Size' Annotation
                        if (value_value1.length < 0) {
                            throw new com.squareup.moshi.JsonDataException("Invalid array length for JSON element 'value1'. Expected minimum: '0', actual minimum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length > 6) {
                            throw new com.squareup.moshi.JsonDataException("Invalid array length for JSON element 'value1'. Expected maximum: '6', actual maximum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length % 2 != 0) {
                            throw new com.squareup.moshi.JsonDataException("Invalid array length for JSON element 'value1'. length of '" + value_value1.length + "' is not a multiple of 2");
                        }

                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestImmutableSize(
            value_value1);
    }

    @Override
    public void writeImpl(JsonWriter writer, TestImmutableSize value) throws IOException {
        // Begin
        writer.beginObject();
        String[] obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String[].class, writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
