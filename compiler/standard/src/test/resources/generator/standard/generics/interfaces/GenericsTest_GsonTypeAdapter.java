package generator.standard.generics.interfaces;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

@GsonPathGenerated
public final class GenericsTest_GsonTypeAdapter extends GsonPathTypeAdapter<GenericsTest> {
    public GenericsTest_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public GenericsTest readImpl(JsonReader reader) throws IOException {
        String value_value1 = null;
        Map<String, Integer> value_value2 = null;
        Double value_value3 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (reader.nextName()) {
                case "value1":
                    value_value1 = moshi.adapter(String.class).fromJson(reader);
                    break;

                case "value2":
                    value_value2 = moshi.<Map<String, Integer>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.Map.class, java.lang.String.class, java.lang.Integer.class)).fromJson(reader);
                    break;

                case "value3":
                    value_value3 = moshi.adapter(Double.class).fromJson(reader);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new GenericsTest_GsonPathModel(
            value_value1,
            value_value2,
            value_value3);
    }

    @Override
    public void writeImpl(JsonWriter writer, GenericsTest value) throws IOException {
        // Begin
        writer.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj0);
        }

        Map<String, Integer> obj1 = value.getValue2();
        if (obj1 != null) {
            writer.name("value2");
            moshi.<Map<String, Integer>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.Map.class, java.lang.String.class, java.lang.Integer.class)).toJson(writer, obj1);
        }

        Double obj2 = value.getValue3();
        if (obj2 != null) {
            writer.name("value3");
            GsonUtil.writeWithGenericAdapter(moshi, Double.class, writer, obj2);
        }

        // End 
        writer.endObject();
    }
}
