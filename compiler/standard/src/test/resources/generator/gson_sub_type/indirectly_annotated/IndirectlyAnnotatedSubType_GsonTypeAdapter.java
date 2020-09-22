package generator.gson_sub_type.indirectly_annotated;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

@GsonPathGenerated
public final class IndirectlyAnnotatedSubType_GsonTypeAdapter extends GsonPathTypeAdapter<IndirectlyAnnotatedSubType> {
    public IndirectlyAnnotatedSubType_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public IndirectlyAnnotatedSubType readImpl(JsonReader reader) throws IOException {
        Map<Object, String> jsonElement = (Map<Object, String>) reader.readJsonValue();

        Object subTypeElement0_jsonElement = jsonElement.get("type");
        final boolean subTypeElement0;
        if (subTypeElement0_jsonElement == null) {
            throw new com.squareup.moshi.JsonDataException("cannot deserialize generator.gson_sub_type.indirectly_annotated.IndirectlyAnnotatedSubType because the subtype field 'type' is either null or does not exist.");
        } else {
            subTypeElement0 = moshi.adapter(boolean.class).fromJsonValue(subTypeElement0_jsonElement);
        }

        Class<? extends IndirectlyAnnotatedSubType> delegateClass = IndirectlyAnnotatedSubType.getSubType1(subTypeElement0);
        if (delegateClass == null) {
            return null;
        }
        IndirectlyAnnotatedSubType result = moshi.adapter(delegateClass).fromJsonValue(jsonElement);
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, IndirectlyAnnotatedSubType value) throws IOException {
        JsonAdapter delegateAdapter = moshi.adapter(value.getClass());
        delegateAdapter.toJson(writer, value);
    }
}
