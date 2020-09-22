package generator.standard.invalid.immutable;

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
public final class TestImmutableRemoveInvalidElements_GsonTypeAdapter extends GsonPathTypeAdapter<TestImmutableRemoveInvalidElements> {
    public TestImmutableRemoveInvalidElements_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestImmutableRemoveInvalidElements readImpl(JsonReader reader) throws IOException {
        String[] value_value1 = null;
        List<String> value_value2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(reader, 1, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (reader.nextName()) {
                case "value1":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, moshi, reader, new RemoveInvalidElementsUtil.CreateArrayFunction<String>() {
                        @Override
                        public String[] createArray() {
                            return new String[0];
                        }
                    });

                    break;

                case "value2":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, moshi, reader);

                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestImmutableRemoveInvalidElements(
            value_value1,
            value_value2);
    }

    @Override
    public void writeImpl(JsonWriter writer, TestImmutableRemoveInvalidElements value) throws
            IOException {
        // Begin
        writer.beginObject();
        String[] obj0 = value.getValue1();
        if (obj0 != null) {
            writer.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, String[].class, writer, obj0);
        }

        List<String> obj1 = value.getValue2();
        if (obj1 != null) {
            writer.name("value2");
            moshi.<List<String>>adapter(com.squareup.moshi.Types.newParameterizedType(java.util.List.class, java.lang.String.class)).toJson(writer, obj1);
        }

        // End 
        writer.endObject();
    }
}
