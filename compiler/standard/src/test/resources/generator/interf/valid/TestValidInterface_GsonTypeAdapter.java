package generator.interf.valid;

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
public final class TestValidInterface_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidInterface> {
    public TestValidInterface_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestValidInterface readImpl(JsonReader reader) throws IOException {
        Integer value_Json1_Nest1 = null;
        Integer value_value2 = null;
        Integer value_Json1_Nest3 = null;
        Integer value_result = null;
        Integer value_that = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 2, 0);

        while (jsonReaderHelper.handleObject(0, 4)) {
            switch (reader.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 2)) {
                        switch (reader.nextName()) {
                            case "Nest1":
                                value_Json1_Nest1 = moshi.adapter(Integer.class).fromJson(reader);
                                break;

                            case "Nest3":
                                value_Json1_Nest3 = moshi.adapter(Integer.class).fromJson(reader);
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "value2":
                    value_value2 = moshi.adapter(Integer.class).fromJson(reader);
                    break;

                case "result":
                    value_result = moshi.adapter(Integer.class).fromJson(reader);
                    break;

                case "that":
                    value_that = moshi.adapter(Integer.class).fromJson(reader);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestValidInterface_GsonPathModel(
                value_Json1_Nest1,
                value_value2,
                value_Json1_Nest3,
                value_result,
                value_that);
    }

    @Override
    public void writeImpl(JsonWriter writer, TestValidInterface value) throws IOException {
        // Begin
        writer.beginObject();

        // Begin Json1
        writer.name("Json1");
        writer.beginObject();
        Integer obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("Nest1");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj0);
        }

        Integer obj1 = value.getValue3();
        if (obj1 != null) {
            writer.name("Nest3");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj1);
        }

        // End Json1
        writer.endObject();
        Integer obj2 = value.getValue2();
        if (obj2 != null) {
            writer.name("value2");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj2);
        }

        Integer obj3 = value.getResult();
        if (obj3 != null) {
            writer.name("result");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj3);
        }

        Integer obj4 = value.getThat();
        if (obj4 != null) {
            writer.name("that");
            GsonUtil.writeWithGenericAdapter(moshi, Integer.class, writer, obj4);
        }

        // End
        writer.endObject();
    }
}
