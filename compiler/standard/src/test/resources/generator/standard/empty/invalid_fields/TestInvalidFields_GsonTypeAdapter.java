package generator.standard.empty.invalid_fields;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestInvalidFields_GsonTypeAdapter extends GsonPathTypeAdapter<TestInvalidFields> {
    public TestInvalidFields_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestInvalidFields readImpl(JsonReader reader) throws IOException {
        TestInvalidFields result = new TestInvalidFields();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestInvalidFields value) throws IOException {
        // Begin
        writer.beginObject();
        // End 
        writer.endObject();
    }
}
