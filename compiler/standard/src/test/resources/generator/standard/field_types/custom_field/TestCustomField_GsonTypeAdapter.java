package generator.standard.field_types.custom_field;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.util.Currency;

@GsonPathGenerated
public final class TestCustomField_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomField> {
    public TestCustomField_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestCustomField readImpl(JsonReader reader) throws IOException {
        TestCustomField result = new TestCustomField();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "value1":
                    Currency value_value1 = moshi.adapter(Currency.class).fromJson(reader);
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
    public void writeImpl(JsonWriter writer, TestCustomField value) throws IOException {
        // Begin
        writer.beginObject();
        Currency obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, Currency.class, writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
