package generator.interf.list;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Override;

public final class TestListInterface_GsonTypeAdapter extends TypeAdapter<TestListInterface> {
    private static final int MANDATORY_INDEX_INTERNALLIST = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    private final Gson mGson;

    public TestListInterface_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestListInterface read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        java.util.List<java.lang.String> value_internalList = null;
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];


        java.util.List<java.lang.String> value_internalList_safe = mGson.getAdapter(new com.google.gson.reflect.TypeToken<java.util.List<java.lang.String>>(){}).read(in);
        if (value_internalList_safe != null) {
            value_internalList = value_internalList_safe;
            mandatoryFieldsCheckList[MANDATORY_INDEX_INTERNALLIST] = true;

        } else {
            throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'internalList' was null for class 'generator.interf.list.TestListInterface_GsonPathModel'");
        }

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case MANDATORY_INDEX_INTERNALLIST:
                        fieldName = "internalList";
                        break;

                }
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.interf.list.TestListInterface_GsonPathModel'");
            }
        }
        return new TestListInterface_GsonPathModel(
                value_internalList
        );
    }

    @Override
    public void write(JsonWriter out, TestListInterface value) throws IOException {
    }
}