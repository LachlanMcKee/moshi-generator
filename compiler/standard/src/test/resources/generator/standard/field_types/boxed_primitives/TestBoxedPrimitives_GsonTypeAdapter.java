package generator.standard.field_types.boxed_primitives;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
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
import java.lang.String;

@GsonPathGenerated
public final class TestBoxedPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestBoxedPrimitives> {
    public TestBoxedPrimitives_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestBoxedPrimitives readImpl(JsonReader reader) throws IOException {
        TestBoxedPrimitives result = new TestBoxedPrimitives();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 9)) {
            switch (reader.nextName()) {
                case "value1":
                    String value_value1 = moshi.adapter(String.class).fromJson(reader);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Boolean value_value2 = moshi.adapter(Boolean.class).fromJson(reader);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Integer value_value3 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    Double value_value4 = moshi.adapter(Double.class).fromJson(reader);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Long value_value5 = moshi.adapter(Long.class).fromJson(reader);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Byte value_value6 = moshi.adapter(Byte.class).fromJson(reader);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Short value_value7 = moshi.adapter(Short.class).fromJson(reader);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    Float value_value8 = moshi.adapter(Float.class).fromJson(reader);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
                    }
                    break;

                case "value9":
                    Character value_value9 = moshi.adapter(Character.class).fromJson(reader);
                    if (value_value9 != null) {
                        result.value9 = value_value9;
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
    public void writeImpl(JsonWriter writer, TestBoxedPrimitives value) throws IOException {
        // Begin
        writer.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj0);
        }

        Boolean obj1 = value.value2;
        if (obj1 != null) {
            writer.name("value2");
            GsonUtil.writeWithGenericAdapter(moshi, Boolean.class, writer, obj1);
        }

        Integer obj2 = value.value3;
        if (obj2 != null) {
            writer.name("value3");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj2);
        }

        Double obj3 = value.value4;
        if (obj3 != null) {
            writer.name("value4");
            GsonUtil.writeWithGenericAdapter(moshi, Double.class, writer, obj3);
        }

        Long obj4 = value.value5;
        if (obj4 != null) {
            writer.name("value5");
            GsonUtil.writeWithGenericAdapter(moshi, Long.class, writer, obj4);
        }

        Byte obj5 = value.value6;
        if (obj5 != null) {
            writer.name("value6");
            GsonUtil.writeWithGenericAdapter(moshi, Byte.class, writer, obj5);
        }

        Short obj6 = value.value7;
        if (obj6 != null) {
            writer.name("value7");
            GsonUtil.writeWithGenericAdapter(moshi, Short.class, writer, obj6);
        }

        Float obj7 = value.value8;
        if (obj7 != null) {
            writer.name("value8");
            GsonUtil.writeWithGenericAdapter(moshi, Float.class, writer, obj7);
        }

        Character obj8 = value.value9;
        if (obj8 != null) {
            writer.name("value9");
            GsonUtil.writeWithGenericAdapter(moshi, Character.class, writer, obj8);
        }

        // End 
        writer.endObject();
    }
}
