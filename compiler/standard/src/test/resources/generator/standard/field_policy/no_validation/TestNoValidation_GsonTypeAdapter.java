package generator.standard.field_policy.no_validation;

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
public final class TestNoValidation_GsonTypeAdapter extends GsonPathTypeAdapter<TestNoValidation> {
    public TestNoValidation_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestNoValidation readImpl(JsonReader reader) throws IOException {
        TestNoValidation result = new TestNoValidation();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (reader.nextName()) {
                case "optional1":
                    Integer value_optional1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_optional1 != null) {
                        result.optional1 = value_optional1;
                    }
                    break;

                case "optional2":
                    Integer value_optional2 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_optional2 != null) {
                        result.optional2 = value_optional2;
                    }
                    break;

                case "optional3":
                    Integer value_optional3 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_optional3 != null) {
                        result.optional3 = value_optional3;
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
    public void writeImpl(JsonWriter writer, TestNoValidation value) throws IOException {
        // Begin
        writer.beginObject();
        Integer obj0 = value.optional1;
        if (obj0 != null) {
            writer.name("optional1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        Integer obj1 = value.optional2;
        if (obj1 != null) {
            writer.name("optional2");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj1);
        }

        int obj2 = value.optional3;
        writer.name("optional3");
        moshi.adapter(Integer.class).toJson(writer, obj2);

        // End 
        writer.endObject();
    }
}
