package generator.standard.field_annotations.exclude;

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
public final class TestExclude_GsonTypeAdapter extends GsonPathTypeAdapter<TestExclude> {
    public TestExclude_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestExclude readImpl(JsonReader reader) throws IOException {
        TestExclude result = new TestExclude();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "element1":
                    Integer value_element1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_element1 != null) {
                        result.element1 = value_element1;
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
    public void writeImpl(JsonWriter writer, TestExclude value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.element1;
        writer.name("element1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
