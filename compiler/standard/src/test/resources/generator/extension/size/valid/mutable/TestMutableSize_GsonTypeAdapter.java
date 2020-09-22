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
public final class TestMutableSize_GsonTypeAdapter extends GsonPathTypeAdapter<TestMutableSize> {
    public TestMutableSize_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestMutableSize readImpl(JsonReader reader) throws IOException {
        TestMutableSize result = new TestMutableSize();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    String[] value_value1 = moshi.adapter(String[].class).fromJson(reader);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }

                    // Gsonpath Extensions
                    if (result.value1 != null) {

                        // Extension - 'Size' Annotation
                        if (result.value1.length != 1) {
                            throw new com.squareup.moshi.JsonDataException("Invalid array length for JSON element 'value1'. Expected length: '1', actual length: '" + result.value1.length + "'");
                        }

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
    public void writeImpl(JsonWriter writer, TestMutableSize value) throws IOException {
        // Begin
        writer.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String[].class, writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
