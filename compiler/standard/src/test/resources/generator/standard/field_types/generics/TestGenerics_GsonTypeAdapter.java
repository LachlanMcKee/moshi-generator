package generator.standard.field_types.generics;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TestGenerics_GsonTypeAdapter extends GsonPathTypeAdapter<TestGenerics> {
    public TestGenerics_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestGenerics readImpl(JsonReader reader) throws IOException {
        TestGenerics result = new TestGenerics();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    List<String> value_value1 = moshi.<List<String>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.List.class, java.lang.String.class)).fromJson(reader);
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
    public void writeImpl(JsonWriter writer, TestGenerics value) throws IOException {
        // Begin
        writer.beginObject();
        List<String> obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            moshi.<List<String>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.List.class, java.lang.String.class)).toJson(writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
