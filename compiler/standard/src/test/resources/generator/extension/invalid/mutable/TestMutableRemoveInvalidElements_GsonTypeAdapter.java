package generator.standard.invalid.mutable;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.extension.RemoveInvalidElementsUtil;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TestMutableRemoveInvalidElements_GsonTypeAdapter extends GsonPathTypeAdapter<TestMutableRemoveInvalidElements> {
    public TestMutableRemoveInvalidElements_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestMutableRemoveInvalidElements readImpl(JsonReader reader) throws IOException {
        TestMutableRemoveInvalidElements result = new TestMutableRemoveInvalidElements();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "value1":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    String[] value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, moshi, reader, new RemoveInvalidElementsUtil.CreateArrayFunction<String>() {
                        @Override
                        public String[] createArray() {
                            return new String[0];
                        }
                    });

                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    List<String> value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, moshi, reader);

                    if (value_value2 != null) {
                        result.value2 = value_value2;
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
    public void writeImpl(JsonWriter writer, TestMutableRemoveInvalidElements value) throws
            IOException {
        // Begin
        writer.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String[].class, writer, obj0);
        }

        List<String> obj1 = value.value2;
        if (obj1 != null) {
            writer.name("value2");
            moshi.<List<String>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.List.class, java.lang.String.class)).toJson(writer, obj1);
        }

        // End 
        writer.endObject();
    }
}
