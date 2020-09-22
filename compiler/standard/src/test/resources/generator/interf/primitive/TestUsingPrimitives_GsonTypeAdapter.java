package generator.interf.primitive;

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
import java.lang.Long;
import java.lang.Override;

@GsonPathGenerated
public final class TestUsingPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestUsingPrimitives> {
    public TestUsingPrimitives_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestUsingPrimitives readImpl(JsonReader reader) throws IOException {
        int value_intExample = 0;
        long value_longExample = 0L;
        double value_doubleExample = 0d;
        boolean value_booleanExample = false;
        int[] value_intArrayExample = null;
        long[] value_longArrayExample = null;
        double[] value_doubleArrayExample = null;
        boolean[] value_booleanArrayExample = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[4];

        while (jsonReaderHelper.handleObject(0, 8)) {
            switch (reader.nextName()) {
                case "intExample":
                    Integer value_intExample_safe = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_intExample_safe != null) {
                        value_intExample = value_intExample_safe;
                        mandatoryFieldsCheckList[0] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("intExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "longExample":
                    Long value_longExample_safe = moshi.adapter(Long.class).fromJson(reader);
                    if (value_longExample_safe != null) {
                        value_longExample = value_longExample_safe;
                        mandatoryFieldsCheckList[1] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("longExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "doubleExample":
                    Double value_doubleExample_safe = moshi.adapter(Double.class).fromJson(reader);
                    if (value_doubleExample_safe != null) {
                        value_doubleExample = value_doubleExample_safe;
                        mandatoryFieldsCheckList[2] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("doubleExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "booleanExample":
                    Boolean value_booleanExample_safe = moshi.adapter(Boolean.class).fromJson(reader);
                    if (value_booleanExample_safe != null) {
                        value_booleanExample = value_booleanExample_safe;
                        mandatoryFieldsCheckList[3] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("booleanExample", "generator.interf.primitive.TestUsingPrimitives_GsonPathModel");
                    }
                    break;

                case "intArrayExample":
                    value_intArrayExample = moshi.adapter(int[].class).fromJson(reader);
                    break;

                case "longArrayExample":
                    value_longArrayExample = moshi.adapter(long[].class).fromJson(reader);
                    break;

                case "doubleArrayExample":
                    value_doubleArrayExample = moshi.adapter(double[].class).fromJson(reader);
                    break;

                case "booleanArrayExample":
                    value_booleanArrayExample = moshi.adapter(boolean[].class).fromJson(reader);
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
    public void writeImpl(JsonWriter writer, TestUsingPrimitives value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.getIntExample();
        writer.name("intExample");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        long obj1 = value.getLongExample();
        writer.name("longExample");
        moshi.adapter(Long.class).toJson(writer, obj1);

        double obj2 = value.getDoubleExample();
        writer.name("doubleExample");
        moshi.adapter(Double.class).toJson(writer, obj2);

        boolean obj3 = value.getBooleanExample();
        writer.name("booleanExample");
        moshi.adapter(Boolean.class).toJson(writer, obj3);

        int[] obj4 = value.getIntArrayExample();
        if (obj4 != null) {
            writer.name("intArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, int[].class, writer, obj4);
        }

        long[] obj5 = value.getLongArrayExample();
        if (obj5 != null) {
            writer.name("longArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, long[].class, writer, obj5);
        }

        double[] obj6 = value.getDoubleArrayExample();
        if (obj6 != null) {
            writer.name("doubleArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, double[].class, writer, obj6);
        }

        boolean[] obj7 = value.getBooleanArrayExample();
        if (obj7 != null) {
            writer.name("booleanArrayExample");
            GsonUtil.writeWithGenericAdapter(moshi, boolean[].class, writer, obj7);
        }

        // End 
        writer.endObject();
    }
}
