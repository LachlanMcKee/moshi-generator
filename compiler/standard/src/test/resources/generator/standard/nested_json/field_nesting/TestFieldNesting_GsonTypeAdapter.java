package generator.standard.nested_json.field_nesting;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestFieldNesting_GsonTypeAdapter extends GsonPathTypeAdapter<TestFieldNesting> {
    public TestFieldNesting_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestFieldNesting readImpl(JsonReader reader) throws IOException {
        TestFieldNesting result = new TestFieldNesting();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "Json1":
                    Integer value_Json1 = moshi.adapter(Integer.class).fromJson(reader);
                    if (value_Json1 != null) {
                        result.value1 = value_Json1;
                    }
                    break;

                case "Json2":
                    while (jsonReaderHelper.handleObject(1, 2)) {
                        switch (reader.nextName()) {
                            case "Nest1":
                                Integer value_Json2_Nest1 = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_Json2_Nest1 != null) {
                                    result.value2 = value_Json2_Nest1;
                                }
                                break;

                            case "Nest2":
                                while (jsonReaderHelper.handleObject(2, 2)) {
                                    switch (reader.nextName()) {
                                        case "EndPoint1":
                                            Integer value_Json2_Nest2_EndPoint1 = moshi.adapter(Integer.class).fromJson(reader);
                                            if (value_Json2_Nest2_EndPoint1 != null) {
                                                result.value3 = value_Json2_Nest2_EndPoint1;
                                            }
                                            break;

                                        case "EndPoint2":
                                            Integer value_Json2_Nest2_EndPoint2 = moshi.adapter(Integer.class).fromJson(reader);
                                            if (value_Json2_Nest2_EndPoint2 != null) {
                                                result.value4 = value_Json2_Nest2_EndPoint2;
                                            }
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

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestFieldNesting value) throws IOException {
        // Begin
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("Json1");
        moshi.adapter(Integer.class).toJson(writer, obj0);


        // Begin Json2
        writer.name("Json2");
        writer.beginObject();
        int obj1 = value.value2;
        writer.name("Nest1");
        moshi.adapter(Integer.class).toJson(writer, obj1);


        // Begin Json2Nest2
        writer.name("Nest2");
        writer.beginObject();
        int obj2 = value.value3;
        writer.name("EndPoint1");
        moshi.adapter(Integer.class).toJson(writer, obj2);

        int obj3 = value.value4;
        writer.name("EndPoint2");
        moshi.adapter(Integer.class).toJson(writer, obj3);

        // End Json2Nest2
        writer.endObject();
        // End Json2
        writer.endObject();
        // End 
        writer.endObject();
    }
}
