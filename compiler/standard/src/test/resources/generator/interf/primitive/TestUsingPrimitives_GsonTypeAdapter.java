package generator.interf.primitive;

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
import java.lang.Long;
import java.lang.Override;

@GsonPathGenerated
public final class TestUsingPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestUsingPrimitives> {

    public TestUsingPrimitives_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestUsingPrimitives readImpl(JsonReader in) throws IOException {
        int value_intExample = 0;
        long value_longExample = 0L;
        double value_doubleExample = 0d;
        boolean value_booleanExample = false;
        int[] value_intArrayExample = null;
        long[] value_longArrayExample = null;
        double[] value_doubleArrayExample = null;
        boolean[] value_booleanArrayExample = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[4];

        while (jsonReaderHelper.handleObject(0, 8)) {
            switch (in.nextName()) {
                case "intExample":
                    Integer value_intExample_safe = moshi.getAdapter(Integer.class).read(in);
                    if (value_intExample_safe != null) {
                        value_intExample = value_intExample_safe;
                        mandatoryFieldsCheckList[0] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("intExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "longExample":
                    Long value_longExample_safe = moshi.getAdapter(Long.class).read(in);
                    if (value_longExample_safe != null) {
                        value_longExample = value_longExample_safe;
                        mandatoryFieldsCheckList[1] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("longExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "doubleExample":
                    Double value_doubleExample_safe = moshi.getAdapter(Double.class).read(in);
                    if (value_doubleExample_safe != null) {
                        value_doubleExample = value_doubleExample_safe;
                        mandatoryFieldsCheckList[2] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("doubleExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "booleanExample":
                    Boolean value_booleanExample_safe = moshi.getAdapter(Boolean.class).read(in);
                    if (value_booleanExample_safe != null) {
                        value_booleanExample = value_booleanExample_safe;
                        mandatoryFieldsCheckList[3] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("booleanExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "intArrayExample":
                    value_intArrayExample = moshi.getAdapter(int[].class).read(in);
                    break;

                case "longArrayExample":
                    value_longArrayExample = moshi.getAdapter(long[].class).read(in);
                    break;

                case "doubleArrayExample":
                    value_doubleArrayExample = moshi.getAdapter(double[].class).read(in);
                    break;

                case "booleanArrayExample":
                    value_booleanArrayExample = moshi.getAdapter(boolean[].class).read(in);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 4; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case 0:
                        fieldName = "intExample";
                        break;

                    case 1:
                        fieldName = "longExample";
                        break;

                    case 2:
                        fieldName = "doubleExample";
                        break;

                    case 3:
                        fieldName = "booleanExample";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
            }
        }
        return new TestUsingPrimitives_GsonPathModel(
                value_intExample,
                value_longExample,
                value_doubleExample,
                value_booleanExample,
                value_intArrayExample,
                value_longArrayExample,
                value_doubleArrayExample,
                value_booleanArrayExample);
    }

    @Override
    public void writeImpl(JsonWriter out, TestUsingPrimitives value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.getIntExample();
        out.name("intExample");
        moshi.getAdapter(Integer.class).write(out, obj0);

        long obj1 = value.getLongExample();
        out.name("longExample");
        moshi.getAdapter(Long.class).write(out, obj1);

        double obj2 = value.getDoubleExample();
        out.name("doubleExample");
        moshi.getAdapter(Double.class).write(out, obj2);

        boolean obj3 = value.getBooleanExample();
        out.name("booleanExample");
        moshi.getAdapter(Boolean.class).write(out, obj3);

        int[] obj4 = value.getIntArrayExample();
        if (obj4 != null) {
            out.name("intArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, obj4.getClass(), out, obj4);
        }

        long[] obj5 = value.getLongArrayExample();
        if (obj5 != null) {
            out.name("longArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, obj5.getClass(), out, obj5);
        }

        double[] obj6 = value.getDoubleArrayExample();
        if (obj6 != null) {
            out.name("doubleArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, obj6.getClass(), out, obj6);
        }

        boolean[] obj7 = value.getBooleanArrayExample();
        if (obj7 != null) {
            out.name("booleanArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, obj7.getClass(), out, obj7);
        }

        // End
        out.endObject();
    }
}
