package generator.standard.generics.interfaces;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Override;

public final class GenericsTest_GsonTypeAdapter extends TypeAdapter<GenericsTest> {
    private final Gson mGson;

    public GenericsTest_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public GenericsTest read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        java.lang.String value_value1 = null;
        java.util.Map<java.lang.String, java.lang.Integer> value_value2 = null;
        java.lang.Double value_value3 = null;

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    value_value1 = getStringSafely(in);
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    value_value2 = mGson.getAdapter(new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Integer>>(){}).read(in);
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    value_value3 = getDoubleSafely(in);
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return new GenericsTest_GsonPathModel(
                value_value1,
                value_value2,
                value_value3
        );
    }

    @Override
    public void write(JsonWriter out, GenericsTest value) throws IOException {
    }
}