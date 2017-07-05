package generator.standard.immutable_class;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

public final class DataClassTest_GsonTypeAdapter extends TypeAdapter<DataClassTest> {
    private static final int MANDATORY_INDEX_VALUE1 = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    private final Gson mGson;

    public DataClassTest_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public DataClassTest read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        java.lang.String value_parent_child_value1 = null;
        boolean value_isBooleanTest1 = false;
        java.lang.Boolean value_isBooleanTest2 = null;
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "parent":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter1 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter1 == 1) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "child":
                                jsonFieldCounter1++;

                                // Ensure the object is not null.
                                if (!isValidValue(in)) {
                                    break;
                                }
                                int jsonFieldCounter2 = 0;
                                in.beginObject();

                                while (in.hasNext()) {
                                    if (jsonFieldCounter2 == 1) {
                                        in.skipValue();
                                        continue;
                                    }

                                    switch (in.nextName()) {
                                        case "value1":
                                            jsonFieldCounter2++;

                                            String value_parent_child_value1_safe = getStringSafely(in);
                                            if (value_parent_child_value1_safe != null) {
                                                value_parent_child_value1 = value_parent_child_value1_safe;
                                                mandatoryFieldsCheckList[MANDATORY_INDEX_VALUE1] = true;

                                            } else {
                                                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'parent.child.value1' was null for class 'generator.standard.immutable_class.DataClassTest'");
                                            }
                                            break;

                                        default:
                                            in.skipValue();
                                            break;
                                    }
                                }

                                in.endObject();
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject();
                    break;

                case "isBooleanTest1":
                    jsonFieldCounter0++;

                    value_isBooleanTest1 = getBooleanSafely(in);
                    break;

                case "isBooleanTest2":
                    jsonFieldCounter0++;

                    value_isBooleanTest2 = getBooleanSafely(in);
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case MANDATORY_INDEX_VALUE1:
                        fieldName = "parent.child.value1";
                        break;

                }
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.standard.immutable_class.DataClassTest'");
            }
        }
        return new DataClassTest(
                value_parent_child_value1,
                value_isBooleanTest1,
                value_isBooleanTest2
        );
    }

    @Override
    public void write(JsonWriter out, DataClassTest value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin parent
        out.name("parent");
        out.beginObject();

        // Begin parentchild
        out.name("child");
        out.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            out.value(obj0);
        }

        // End parentchild
        out.endObject();
        // End parent
        out.endObject();
        boolean obj1 = value.isBooleanTest1();
        out.name("isBooleanTest1");
        out.value(obj1);

        Boolean obj2 = value.isBooleanTest2();
        if (obj2 != null) {
            out.name("isBooleanTest2");
            out.value(obj2);
        }

        // End
        out.endObject();
    }
}