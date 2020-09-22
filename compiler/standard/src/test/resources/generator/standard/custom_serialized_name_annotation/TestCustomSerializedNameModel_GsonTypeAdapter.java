package generator.standard.custom_serialized_name_annotation;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestCustomSerializedNameModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomSerializedNameModel> {
    public TestCustomSerializedNameModel_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestCustomSerializedNameModel readImpl(JsonReader reader) throws IOException {
        TestCustomSerializedNameModel result = new TestCustomSerializedNameModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (reader.nextName()) {
                case "nest":
                    while (jsonReaderHelper.handleObject(1, 5)) {
                        switch (reader.nextName()) {
                            case "value1":
                                String value_nest_value1 = moshi.adapter(String.class).fromJson(reader);
                                if (value_nest_value1 != null) {
                                    result.value1 = value_nest_value1;
                                }
                                break;

                            case "value2":
                                String value_nest_value2 = moshi.adapter(String.class).fromJson(reader);
                                if (value_nest_value2 != null) {
                                    result.valueX = value_nest_value2;
                                }
                                break;

                            case "second":
                                while (jsonReaderHelper.handleObject(2, 2)) {
                                    switch (reader.nextName()) {
                                        case "value3":
                                            String value_nest_second_value3 = moshi.adapter(String.class).fromJson(reader);
                                            if (value_nest_second_value3 != null) {
                                                result.value3 = value_nest_second_value3;
                                            }
                                            break;

                                        case "value3b":
                                            String value_nest_second_value3b = moshi.adapter(String.class).fromJson(reader);
                                            if (value_nest_second_value3b != null) {
                                                result.value3b = value_nest_second_value3b;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
                                }
                                break;

                            case "value1b":
                                String value_nest_value1b = moshi.adapter(String.class).fromJson(reader);
                                if (value_nest_value1b != null) {
                                    result.value1b = value_nest_value1b;
                                }
                                break;

                            case "value2b":
                                String value_nest_value2b = moshi.adapter(String.class).fromJson(reader);
                                if (value_nest_value2b != null) {
                                    result.valueXb = value_nest_value2b;
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
    public void writeImpl(JsonWriter writer, TestCustomSerializedNameModel value) throws
            IOException {
        // Begin
        writer.beginObject();

        // Begin nest
        writer.name("nest");
        writer.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj0);
        }

        String obj1 = value.valueX;
        if (obj1 != null) {
            writer.name("value2");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj1);
        }


        // Begin nestsecond
        writer.name("second");
        writer.beginObject();
        String obj2 = value.value3;
        if (obj2 != null) {
            writer.name("value3");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj2);
        }

        String obj3 = value.value3b;
        if (obj3 != null) {
            writer.name("value3b");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj3);
        }

        // End nestsecond
        writer.endObject();
        String obj4 = value.value1b;
        if (obj4 != null) {
            writer.name("value1b");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj4);
        }

        String obj5 = value.valueXb;
        if (obj5 != null) {
            writer.name("value2b");
            GsonUtil.writeWithGenericAdapter(moshi, String.class, writer, obj5);
        }

        // End nest
        writer.endObject();
        // End 
        writer.endObject();
    }
}
