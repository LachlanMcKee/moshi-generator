package generator.standard.nested_class;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNestedClass_Nested_GsonTypeAdapter extends GsonPathTypeAdapter<TestNestedClass.Nested> {
    public TestNestedClass_Nested_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNestedClass.Nested readImpl(JsonReader in) throws IOException {
        TestNestedClass.Nested result = new TestNestedClass.Nested();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    Integer value_value1 = moshi.getAdapter(Integer.class).read(in);
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
    public void writeImpl(JsonWriter out, TestNestedClass.Nested value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("value1");
        moshi.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}
