package generator.interf.inheritance;

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
public final class TestUsingInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestUsingInheritance> {
    public TestUsingInheritance_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestUsingInheritance readImpl(JsonReader reader) throws IOException {
        Integer value_value3 = null;
        Integer value_value1 = null;
        Integer value_Json1_Nest2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (reader.nextName()) {
                case "value3":
                    value_value3 = moshi.adapter(Integer.class).fromJson(reader);
                    break;

                case "value1":
                    Integer value_value1_safe = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_value1_safe != null) {
                        value_value1 = value_value1_safe;
                        mandatoryFieldsCheckList[0] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("value1", "generator.interf.inheritance.TestUsingInheritance_GsonPathModel");
                    }
                    break;

                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "Nest2":
                                value_Json1_Nest2 = moshi.adapter(Integer.class).fromJson(reader);
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
                        fieldName = "value1";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.interf.inheritance.TestUsingInheritance_GsonPathModel");
            }
        }
        return new TestUsingInheritance_GsonPathModel(
            value_value3,
            value_value1,
            value_Json1_Nest2);
    }

    @Override
    public void writeImpl(JsonWriter writer, TestUsingInheritance value) throws IOException {
        // Begin
        writer.beginObject();
        Integer obj0 = value.getValue3();
        if (obj0 != null) {
            writer.name("value3");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        Integer obj1 = value.getValue1();
        if (obj1 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj1);
        }


        // Begin Json1
        writer.name("Json1");
        writer.beginObject();
        Integer obj2 = value.getValue2();
        if (obj2 != null) {
            writer.name("Nest2");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj2);
        }

        // End Json1
        writer.endObject();
        // End 
        writer.endObject();
    }
}
