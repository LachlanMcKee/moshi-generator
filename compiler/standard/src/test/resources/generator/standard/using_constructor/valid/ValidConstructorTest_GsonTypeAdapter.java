package generator.standard.using_constructor.valid;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class ValidConstructorTest_GsonTypeAdapter extends GsonPathTypeAdapter<ValidConstructorTest> {
    public ValidConstructorTest_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public ValidConstructorTest readImpl(JsonReader reader) throws IOException {
        String value_parent_child_value1 = null;
        boolean value_isBooleanTest1 = false;
        Boolean value_isBooleanTest2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (reader.nextName()) {
                case "parent":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "child":
                                while (jsonReaderHelper.handleObject(2, 1)) {
                                    switch (reader.nextName()) {
                                        case "value1":
                                            value_parent_child_value1 = moshi.adapter(String.class).fromJson(reader);
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "isBooleanTest1":
                    value_isBooleanTest1 = moshi.adapter(Boolean.class).fromJson(reader);
                    break;

                case "isBooleanTest2":
                    value_isBooleanTest2 = moshi.adapter(Boolean.class).fromJson(reader);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new ValidConstructorTest(
            value_parent_child_value1,
            value_isBooleanTest1,
            value_isBooleanTest2);
    }

    @Override
    public void writeImpl(JsonWriter writer, ValidConstructorTest value) throws IOException {
        // Begin
        writer.beginObject();

        // Begin parent
        writer.name("parent");
        writer.beginObject();

        // Begin parentchild
        writer.name("child");
        writer.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj0);
        }

        // End parentchild
        writer.endObject();
        // End parent
        writer.endObject();
        boolean obj1 = value.isBooleanTest1();
        writer.name("isBooleanTest1");
        moshi.adapter(Boolean.class).toJson(writer, obj1);

        Boolean obj2 = value.isBooleanTest2();
        if (obj2 != null) {
            writer.name("isBooleanTest2");
            GsonUtil.writeWithGenericAdapter(moshi, Boolean.class, writer, obj2);
        }

        // End 
        writer.endObject();
    }
}
