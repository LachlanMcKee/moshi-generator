package generator.standard.nested_json.root_nesting;

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
public final class TestRootNesting_GsonTypeAdapter extends GsonPathTypeAdapter<TestRootNesting> {
    public TestRootNesting_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestRootNesting readImpl(JsonReader reader) throws IOException {
        TestRootNesting result = new TestRootNesting();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "Root":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "Nest1":
                                while (jsonReaderHelper.handleObject(2, 1)) {
                                    switch (reader.nextName()) {
                                        case "value1":
                                            Integer value_value1 = moshi.adapter(Integer.class).fromJson(reader);
                                            if (value_value1 != null) {
                                                result.value1 = value_value1;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
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
    public void writeImpl(JsonWriter writer, TestRootNesting value) throws IOException {
        // Begin
        writer.beginObject();

        // Begin Root
        writer.name("Root");
        writer.beginObject();

        // Begin RootNest1
        writer.name("Nest1");
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("value1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End RootNest1
        writer.endObject();
        // End Root
        writer.endObject();
        // End 
        writer.endObject();
    }
}
