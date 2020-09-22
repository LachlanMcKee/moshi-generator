package generator.standard.field_policy.validate_explicit_non_null;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestValidateWithDefaultValue_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateWithDefaultValue> {
    public TestValidateWithDefaultValue_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestValidateWithDefaultValue readImpl(JsonReader reader) throws IOException {
        TestValidateWithDefaultValue result = new TestValidateWithDefaultValue();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "mandatoryWithDefault":
                    Integer value_mandatoryWithDefault = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_mandatoryWithDefault != null) {
                        result.mandatoryWithDefault = value_mandatoryWithDefault;
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
    public void writeImpl(JsonWriter writer, TestValidateWithDefaultValue value) throws
            IOException {
        // Begin
        writer.beginObject();
        Integer obj0 = value.mandatoryWithDefault;
        if (obj0 != null) {
            writer.name("mandatoryWithDefault");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        // End 
        writer.endObject();
    }
}
