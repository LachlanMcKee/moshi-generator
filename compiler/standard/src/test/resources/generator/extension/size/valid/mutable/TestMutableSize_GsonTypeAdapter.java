package generator.standard.size.valid.nullable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;

import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestMutableSize_GsonTypeAdapter extends GsonPathTypeAdapter<TestMutableSize> {
    public TestMutableSize_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestMutableSize readImpl(JsonReader in) throws IOException {
        TestMutableSize result = new TestMutableSize();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    String[] value_value1 = moshi.getAdapter(String[].class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }

                    // Gsonpath Extensions
                    if (result.value1 != null) {

                        // Extension - 'Size' Annotation
                        if (result.value1.length != 1) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected length: '1', actual length: '" + result.value1.length + "'");
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
    public void writeImpl(JsonWriter out, TestMutableSize value) throws IOException {
        // Begin
        out.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}
