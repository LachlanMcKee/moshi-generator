package generator.interf.java8;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestJava8Interface_GsonTypeAdapter extends GsonPathTypeAdapter<TestJava8Interface> {
    public TestJava8Interface_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestJava8Interface readImpl(JsonReader reader) throws IOException {
        Integer value_value1 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    value_value1 = moshi.adapter(Integer.class).fromJson(reader);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestJava8Interface_GsonPathModel(
            value_value1);
    }

    @Override
    public void writeImpl(JsonWriter writer, TestJava8Interface value) throws IOException {
        // Begin
        writer.beginObject();
        Integer obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
