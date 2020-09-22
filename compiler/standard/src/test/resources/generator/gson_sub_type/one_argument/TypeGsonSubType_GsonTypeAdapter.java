package generator.gson_sub_type.one_argument;

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
public final class TypeGsonSubType_GsonTypeAdapter extends GsonPathTypeAdapter<TypeGsonSubType> {
    public TypeGsonSubType_GsonTypeAdapter(Moshi moshi) {
        super(moshi);
    }

    @Override
    public TypeGsonSubType readImpl(JsonReader reader) throws IOException {
        Map<Object, String> jsonElement = (Map<Object, String>) reader.readJsonValue();

        Object subTypeElement0_jsonElement = jsonElement.get("type");
        final String subTypeElement0;
        if (subTypeElement0_jsonElement == null) {
            subTypeElement0 = null;
        } else {
            subTypeElement0 = moshi.adapter(String.class).fromJsonValue(subTypeElement0_jsonElement);
        }

        Class<? extends TypeGsonSubType> delegateClass = TypeGsonSubType.getSubType1(subTypeElement0);
        if (delegateClass == null) {
            return null;
        }
        TypeGsonSubType result = moshi.adapter(delegateClass).fromJsonValue(jsonElement);
        return result;
    }

    @Override
    public void writeImpl(JsonWriter writer, TypeGsonSubType value) throws IOException {
        JsonAdapter delegateAdapter = moshi.adapter(value.getClass());
        delegateAdapter.toJson(writer, value);
    }
}
