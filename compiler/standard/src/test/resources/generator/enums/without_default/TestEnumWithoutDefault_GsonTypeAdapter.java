package generator.enums.without_default;

import static gsonpath.internal.GsonUtil.*;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestEnumWithoutDefault_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnumWithoutDefault> {
    public TestEnumWithoutDefault_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestEnumWithoutDefault readImpl(JsonReader reader) throws IOException {
        String enumValue = reader.nextString();
        switch (enumValue) {
            case "value-abc":
                return TestEnumWithoutDefault.VALUE_ABC;

            case "value-def":
                return TestEnumWithoutDefault.VALUE_DEF;

            case "custom":
                return TestEnumWithoutDefault.VALUE_GHI;

            case "value-1":
                return TestEnumWithoutDefault.VALUE_1;

            default:
                throw new gsonpath.exception.JsonUnexpectedEnumValueException(enumValue, "generator.enums.without_default.TestEnumWithoutDefault");

        }
    }

    @Override
    public void writeImpl(JsonWriter writer, TestEnumWithoutDefault value) throws IOException {
        switch (value) {
            case VALUE_ABC:
                writer.value("value-abc");
                break;

            case VALUE_DEF:
                writer.value("value-def");
                break;

            case VALUE_GHI:
                writer.value("custom");
                break;

            case VALUE_1:
                writer.value("value-1");
                break;

        }
    }
}
