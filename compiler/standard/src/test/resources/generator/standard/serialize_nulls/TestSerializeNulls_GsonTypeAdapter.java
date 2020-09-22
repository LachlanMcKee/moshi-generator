package generator.standard.class_annotations.serialize_nulls;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestSerializeNulls_GsonTypeAdapter extends GsonPathTypeAdapter<TestSerializeNulls> {
    public TestSerializeNulls_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestSerializeNulls readImpl(JsonReader reader) throws IOException {
        TestSerializeNulls result = new TestSerializeNulls();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 7)) {
            switch (reader.nextName()) {
                case "value1":
                    Integer value_value1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Double value_value2 = moshi.adapter(Double.class).fromJson(reader);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Boolean value_value3 = moshi.adapter(Boolean.class).fromJson(reader);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    String value_value4 = moshi.adapter(String.class).fromJson(reader);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Integer value_value5 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Double value_value6 = moshi.adapter(Double.class).fromJson(reader);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Boolean value_value7 = moshi.adapter(Boolean.class).fromJson(reader);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
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
    public void writeImpl(JsonWriter writer, TestSerializeNulls value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("value1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        double obj1 = value.value2;
        writer.name("value2");
        moshi.adapter(Double.class).toJson(writer, obj1);

        boolean obj2 = value.value3;
        writer.name("value3");
        moshi.adapter(Boolean.class).toJson(writer, obj2);

        String obj3 = value.value4;
        writer.name("value4");
        if (obj3 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj3);
        } else {
            writer.nullValue();
        }

        Integer obj4 = value.value5;
        writer.name("value5");
        if (obj4 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj4);
        } else {
            writer.nullValue();
        }

        Double obj5 = value.value6;
        writer.name("value6");
        if (obj5 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, Double.class, writer, obj5);
        } else {
            writer.nullValue();
        }

        Boolean obj6 = value.value7;
        writer.name("value7");
        if (obj6 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, Boolean.class, writer, obj6);
        } else {
            writer.nullValue();
        }

        // End 
        writer.endObject();
    }
}
