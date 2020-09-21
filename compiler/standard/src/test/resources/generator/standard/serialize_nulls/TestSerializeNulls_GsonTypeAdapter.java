package generator.standard.class_annotations.serialize_nulls;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
    public TestSerializeNulls_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestSerializeNulls readImpl(JsonReader in) throws IOException {
        TestSerializeNulls result = new TestSerializeNulls();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 7)) {
            switch (in.nextName()) {
                case "value1":
                    Integer value_value1 = moshi.getAdapter(Integer.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Double value_value2 = moshi.getAdapter(Double.class).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Boolean value_value3 = moshi.getAdapter(Boolean.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    String value_value4 = moshi.getAdapter(String.class).read(in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Integer value_value5 = moshi.getAdapter(Integer.class).read(in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Double value_value6 = moshi.getAdapter(Double.class).read(in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Boolean value_value7 = moshi.getAdapter(Boolean.class).read(in);
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
    public void writeImpl(JsonWriter out, TestSerializeNulls value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("value1");
        moshi.getAdapter(Integer.class).write(out, obj0);

        double obj1 = value.value2;
        out.name("value2");
        moshi.getAdapter(Double.class).write(out, obj1);

        boolean obj2 = value.value3;
        out.name("value3");
        moshi.getAdapter(Boolean.class).write(out, obj2);

        String obj3 = value.value4;
        out.name("value4");
        if (obj3 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, obj3.getClass(), out, obj3);
        } else {
            out.nullValue();
        }

        Integer obj4 = value.value5;
        out.name("value5");
        if (obj4 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, obj4.getClass(), out, obj4);
        } else {
            out.nullValue();
        }

        Double obj5 = value.value6;
        out.name("value6");
        if (obj5 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, obj5.getClass(), out, obj5);
        } else {
            out.nullValue();
        }

        Boolean obj6 = value.value7;
        out.name("value7");
        if (obj6 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, obj6.getClass(), out, obj6);
        } else {
            out.nullValue();
        }

        // End
        out.endObject();
    }
}
