package generator.standard.invalid.immutable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
    public TestImmutableRemoveInvalidElements_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestImmutableRemoveInvalidElements readImpl(JsonReader in) throws IOException {
        String[] value_value1 = null;
        List<String> value_value2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "value1":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, moshi, in, new RemoveInvalidElementsUtil.CreateArrayFunction<String>() {
                        @Override
                        public String[] createArray() {
                            return new String[0];
                        }
                    });

                    break;

                case "value2":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, moshi, in);

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
    public void writeImpl(JsonWriter out, TestImmutableRemoveInvalidElements value) throws
            IOException {
        // Begin
        out.beginObject();
        String[] obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(moshi, obj0.getClass(), out, obj0);
        }

        List<String> obj1 = value.getValue2();
        if (obj1 != null) {
            out.name("value2");
            moshi.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj1);
        }

        // End
        out.endObject();
    }
}
