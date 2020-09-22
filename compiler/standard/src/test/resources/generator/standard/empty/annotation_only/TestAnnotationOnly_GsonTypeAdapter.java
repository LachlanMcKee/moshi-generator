package generator.standard.empty.annotation_only;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestAnnotationOnly_GsonTypeAdapter extends GsonPathTypeAdapter<TestAnnotationOnly> {
    public TestAnnotationOnly_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestAnnotationOnly readImpl(JsonReader reader) throws IOException {
        TestAnnotationOnly result = new TestAnnotationOnly();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestAnnotationOnly value) throws IOException {
        // Begin
        writer.beginObject();
        // End 
        writer.endObject();
    }
}
