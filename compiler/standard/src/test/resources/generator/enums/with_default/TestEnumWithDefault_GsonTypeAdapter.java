package generator.enums.with_default;

import static gsonpath.internal.GsonUtil.*;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.audit.AuditLog;
import gsonpath.internal.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestEnumWithDefault_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnumWithDefault> {
    public TestEnumWithDefault_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TestEnumWithDefault readImpl(JsonReader reader) throws IOException {
        String enumValue = reader.nextString();
        switch (enumValue) {
            case "value-abc":
                return TestEnumWithDefault.VALUE_ABC;

            case "value-def":
                return TestEnumWithDefault.VALUE_DEF;

            case "custom":
                return TestEnumWithDefault.VALUE_GHI;

            case "value-1":
                return TestEnumWithDefault.VALUE_1;

            default:
                AuditLog auditLog = AuditLog.fromReader(reader);
                if (auditLog != null) {
                    auditLog.addUnexpectedEnumValue(new AuditLog.UnexpectedEnumValue("generator.enums.with_default.TestEnumWithDefault", reader.getPath(), enumValue));
                }
                return TestEnumWithDefault.VALUE_ABC;

        }
    }

    @Override
    public void writeImpl(JsonWriter writer, TestEnumWithDefault value) throws IOException {
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
