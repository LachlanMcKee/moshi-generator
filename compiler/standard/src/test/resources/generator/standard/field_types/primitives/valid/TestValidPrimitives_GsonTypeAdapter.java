package generator.standard.field_types.primitives.valid;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Byte;
import java.lang.Character;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.Short;

@GsonPathGenerated
public final class TestValidPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidPrimitives> {
    public TestValidPrimitives_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestValidPrimitives readImpl(JsonReader reader) throws IOException {
        TestValidPrimitives result = new TestValidPrimitives();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 8)) {
            switch (reader.nextName()) {
                case "value1":
                    Boolean value_value1 = moshi.adapter(Boolean.class).fromJson(reader);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Integer value_value2 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Double value_value3 = moshi.adapter(Double.class).fromJson(reader);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    Long value_value4 = moshi.adapter(Long.class).fromJson(reader);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Byte value_value5 = moshi.adapter(Byte.class).fromJson(reader);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Short value_value6 = moshi.adapter(Short.class).fromJson(reader);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Float value_value7 = moshi.adapter(Float.class).fromJson(reader);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    Character value_value8 = moshi.adapter(Character.class).fromJson(reader);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
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
    public void writeImpl(JsonWriter writer, TestValidPrimitives value) throws IOException {
        // Begin
        writer.beginObject();
        boolean obj0 = value.value1;
        writer.name("value1");
        moshi.adapter(Boolean.class).toJson(writer, obj0);

        int obj1 = value.value2;
        writer.name("value2");
        moshi.adapter(Integer.class).toJson(writer, obj1);

        double obj2 = value.value3;
        writer.name("value3");
        moshi.adapter(Double.class).toJson(writer, obj2);

        long obj3 = value.value4;
        writer.name("value4");
        moshi.adapter(Long.class).toJson(writer, obj3);

        byte obj4 = value.value5;
        writer.name("value5");
        moshi.adapter(Byte.class).toJson(writer, obj4);

        short obj5 = value.value6;
        writer.name("value6");
        moshi.adapter(Short.class).toJson(writer, obj5);

        float obj6 = value.value7;
        writer.name("value7");
        moshi.adapter(Float.class).toJson(writer, obj6);

        char obj7 = value.value8;
        writer.name("value8");
        moshi.adapter(Character.class).toJson(writer, obj7);

        // End 
        writer.endObject();
    }
}
