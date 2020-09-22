package generator.standard.use_getter_annotation;

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
import java.lang.String;

@GsonPathGenerated
public final class UseGetterAnnotationTest_Implementation_GsonTypeAdapter extends GsonPathTypeAdapter<UseGetterAnnotationTest.Implementation> {
    public UseGetterAnnotationTest_Implementation_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public UseGetterAnnotationTest.Implementation readImpl(JsonReader reader) throws IOException {
        String value_common_name = null;
        int value_specific_intTest = 0;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "common":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "name":
                                String value_common_name_safe = moshi.adapter(String.class).fromJson(reader);
                                if (value_common_name_safe != null) {
                                    value_common_name = value_common_name_safe;
                                    mandatoryFieldsCheckList[0] = true;

                                } else {
                                    throw new gsonpath.exception.JsonFieldNullException("common.name", "generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation");
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "specific":
                    while (jsonReaderHelper.handleObject(2, 1)) {
                        switch (reader.nextName()) {
                            case "intTest":
                                value_specific_intTest = moshi.adapter(Integer.class).fromJson(reader);
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(2);
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
                        fieldName = "common.name";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation");
            }
        }
        return new UseGetterAnnotationTest.Implementation(
            value_common_name,
            value_specific_intTest);
    }

    @Override
    public void writeImpl(JsonWriter writer, UseGetterAnnotationTest.Implementation value) throws
            IOException {
        // Begin
        writer.beginObject();

        // Begin common
        writer.name("common");
        writer.beginObject();
        String obj0 = value.getName();
        if (obj0 != null) {
            writer.name("name");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj0);
        }

        // End common
        writer.endObject();

        // Begin specific
        writer.name("specific");
        writer.beginObject();
        int obj1 = value.getIntTest();
        writer.name("intTest");
        moshi.adapter(Integer.class).toJson(writer, obj1);

        // End specific
        writer.endObject();
        // End 
        writer.endObject();
    }
}
