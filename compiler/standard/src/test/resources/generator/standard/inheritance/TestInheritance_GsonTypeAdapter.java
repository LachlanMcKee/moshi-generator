package generator.standard.inheritance;

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
public final class TestInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestInheritance> {
    public TestInheritance_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestInheritance readImpl(JsonReader reader) throws IOException {
        TestInheritance result = new TestInheritance();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "Json1":
                    Integer value_Json1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_Json1 != null) {
                        result.value1 = value_Json1;
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
    public void writeImpl(JsonWriter writer, TestInheritance value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("Json1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End 
        writer.endObject();
    }
}
