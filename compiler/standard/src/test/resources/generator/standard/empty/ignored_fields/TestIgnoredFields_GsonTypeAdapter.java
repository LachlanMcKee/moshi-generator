package generator.standard.empty.ignored_fields;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestIgnoredFields_GsonTypeAdapter extends GsonPathTypeAdapter<TestIgnoredFields> {
    public TestIgnoredFields_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestIgnoredFields readImpl(JsonReader reader) throws IOException {
        TestIgnoredFields result = new TestIgnoredFields();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestIgnoredFields value) throws IOException {
        // Begin
        writer.beginObject();
        // End 
        writer.endObject();
    }
}
