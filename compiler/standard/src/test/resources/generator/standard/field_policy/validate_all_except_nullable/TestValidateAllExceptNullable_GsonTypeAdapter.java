package generator.standard.field_policy.validate_all_except_nullable;

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
public final class TestValidateAllExceptNullable_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateAllExceptNullable> {
    public TestValidateAllExceptNullable_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestValidateAllExceptNullable readImpl(JsonReader reader) throws IOException {
        TestValidateAllExceptNullable result = new TestValidateAllExceptNullable();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[2];

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (reader.nextName()) {
                case "mandatory1":
                    Integer value_mandatory1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_mandatory1 != null) {
                        result.mandatory1 = value_mandatory1;
                        mandatoryFieldsCheckList[0] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("mandatory1", "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
                    }
                    break;

                case "mandatory2":
                    Integer value_mandatory2 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_mandatory2 != null) {
                        result.mandatory2 = value_mandatory2;
                        mandatoryFieldsCheckList[1] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("mandatory2", "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
                    }
                    break;

                case "optional1":
                    Integer value_optional1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_optional1 != null) {
                        result.optional1 = value_optional1;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 2; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case 0:
                        fieldName = "mandatory1";
                        break;

                    case 1:
                        fieldName = "mandatory2";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestValidateAllExceptNullable value) throws
            IOException {
        // Begin
        writer.beginObject();
        Integer obj0 = value.mandatory1;
        if (obj0 != null) {
            writer.name("mandatory1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        Integer obj1 = value.mandatory2;
        if (obj1 != null) {
            writer.name("mandatory2");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj1);
        }

        Integer obj2 = value.optional1;
        if (obj2 != null) {
            writer.name("optional1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj2);
        }

        // End 
        writer.endObject();
    }
}
