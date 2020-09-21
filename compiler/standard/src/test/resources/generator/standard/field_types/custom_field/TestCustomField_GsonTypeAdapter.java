package generator.standard.field_types.custom_field;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;

import java.io.IOException;
import java.lang.Override;
import java.util.Currency;

@GsonPathGenerated
public final class TestCustomField_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomField> {
    public TestCustomField_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestCustomField readImpl(JsonReader in) throws IOException {
        TestCustomField result = new TestCustomField();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    Currency value_value1 = moshi.getAdapter(Currency.class).read(in);
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
    public void writeImpl(JsonWriter out, TestCustomField value) throws IOException {
        // Begin
        out.beginObject();
        Currency obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}
