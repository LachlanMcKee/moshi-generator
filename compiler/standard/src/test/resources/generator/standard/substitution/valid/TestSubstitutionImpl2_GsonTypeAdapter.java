package generator.standard.substitution.valid;

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
public final class TestSubstitutionImpl1_GsonTypeAdapter extends GsonPathTypeAdapter<TestSubstitutionImpl1> {
    public TestSubstitutionImpl1_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestSubstitutionImpl1 readImpl(JsonReader reader) throws IOException {
        TestSubstitutionImpl1 result = new TestSubstitutionImpl1();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 3, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "Impl1_A":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (reader.nextName()) {
                            case "Json1":
                                Integer value_Impl1_A_Json1 = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_Impl1_A_Json1 != null) {
                                    result.value1 = value_Impl1_A_Json1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "Impl1_B":
                    while (jsonReaderHelper.handleObject(2, 1)) {
                        switch (reader.nextName()) {
                            case "Json1":
                                Integer value_Impl1_B_Json1 = moshi.adapter(Integer.class).fromJson(reader);
                                if (value_Impl1_B_Json1 != null) {
                                    result.value2 = value_Impl1_B_Json1;
                                }
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
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TestSubstitutionImpl1 value) throws IOException {
        // Begin
        writer.beginObject();

        // Begin Impl1_A
        writer.name("Impl1_A");
        writer.beginObject();
        int obj0 = value.value1;
        writer.name("Json1");
        moshi.adapter(Integer.class).toJson(writer, obj0);

        // End Impl1_A
        writer.endObject();

        // Begin Impl1_B
        writer.name("Impl1_B");
        writer.beginObject();
        int obj1 = value.value2;
        writer.name("Json1");
        moshi.adapter(Integer.class).toJson(writer, obj1);

        // End Impl1_B
        writer.endObject();
        // End 
        writer.endObject();
    }
}
