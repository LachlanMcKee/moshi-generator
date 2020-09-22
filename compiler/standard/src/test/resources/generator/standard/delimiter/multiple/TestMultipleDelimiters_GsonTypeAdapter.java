package generator.standard.delimiter.multiple;

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
public final class TestMultipleDelimiters_GsonTypeAdapter extends GsonPathTypeAdapter<TestMultipleDelimiters> {
    public TestMultipleDelimiters_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestMultipleDelimiters readImpl(JsonReader reader) throws IOException {
        TestMultipleDelimiters result = new TestMultipleDelimiters();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 2, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "Nest1":
                                Integer value_Json1_Nest1 = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_Json1_Nest1 != null) {
                                    result.value1 = value_Json1_Nest1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "Json2.Nest1":
                    Integer value_Json2_Nest1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_Json2_Nest1 != null) {
                        result.value2 = value_Json2_Nest1;
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
    public void writeImpl(JsonWriter writer, TestMultipleDelimiters value) throws IOException {
        // Begin
        writer.beginObject();

        // Begin Json1
        writer.name("Json1");
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("Nest1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End Json1
        writer.endObject();
        int obj1 = value.value2;
        writer.name("Json2.Nest1");
        moshi.adapter(Integer.class).toJson(writer, obj1);

        // End 
        writer.endObject();
    }
}
