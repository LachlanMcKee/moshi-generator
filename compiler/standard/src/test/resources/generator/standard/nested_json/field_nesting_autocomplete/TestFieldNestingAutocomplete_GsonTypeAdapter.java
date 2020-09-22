package generator.standard.nested_json.field_nesting_autocomplete;

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
public final class TestFieldNestingAutocomplete_GsonTypeAdapter extends GsonPathTypeAdapter<TestFieldNestingAutocomplete> {
    public TestFieldNestingAutocomplete_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestFieldNestingAutocomplete readImpl(JsonReader reader) throws IOException {
        TestFieldNestingAutocomplete result = new TestFieldNestingAutocomplete();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 2, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "value1":
                                Integer value_Json1_value1 = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_Json1_value1 != null) {
                                    result.value1 = value_Json1_value1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

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
    public void writeImpl(JsonWriter writer, TestFieldNestingAutocomplete value) throws
            IOException {
        // Begin
        writer.beginObject();

        // Begin Json1
        writer.name("Json1");
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("value1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End Json1
        writer.endObject();
        // End 
        writer.endObject();
    }
}
