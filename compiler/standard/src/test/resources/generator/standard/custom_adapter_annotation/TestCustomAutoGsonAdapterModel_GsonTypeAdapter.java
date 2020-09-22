package generator.standard.custom_adapter_annotation;

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
public final class TestCustomAutoGsonAdapterModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomAutoGsonAdapterModel> {
    public TestCustomAutoGsonAdapterModel_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestCustomAutoGsonAdapterModel readImpl(JsonReader reader) throws IOException {
        TestCustomAutoGsonAdapterModel result = new TestCustomAutoGsonAdapterModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "path":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "expectedValue":
                                Integer value_path_expectedValue = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_path_expectedValue != null) {
                                    result.expectedValue = value_path_expectedValue;
                                    mandatoryFieldsCheckList[0] = true;

                                } else {
                                    throw new gsonpath.exception.JsonFieldNullException("path$expectedValue", "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 1; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case 0:
                        fieldName = "path$expectedValue";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestCustomAutoGsonAdapterModel value) throws
            IOException {
        // Begin
        writer.beginObject();

        // Begin path
        writer.name("path");
        writer.beginObject();
        Integer obj0 = value.expectedValue;
        writer.name("expectedValue");
        if (obj0 != null) {
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        } else {
            writer.nullValue();
        }

        // End path
        writer.endObject();
        // End
        writer.endObject();
    }
}
