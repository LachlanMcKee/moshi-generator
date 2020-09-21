package gsonpath.extension;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import gsonpath.audit.AuditLog;
import gsonpath.audit.AuditLog.RemovedElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.internal.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {

    public static <T> void removeInvalidElementsList(JsonAdapter<T> adapter, JsonReader reader, List<T> outputList) throws IOException {
        AuditLog auditLog = AuditLog.fromReader(reader);

        List<Object> jsonArray = (List<Object>) reader.readJsonValue();
        for (Object jsonElement : jsonArray) {
            try {
                outputList.add(adapter.fromJsonValue(jsonElement));
            } catch (Exception e) {
                auditLog.addRemovedElement(new RemovedElement(reader.getPath(), e, jsonElement));
            }
        }
    }

    public static <T> List<T> removeInvalidElementsList(Class<T> clazz, Moshi moshi, JsonReader reader) throws IOException {
        if (!isValidValue(reader)) {
            return null;
        }
        JsonAdapter<T> adapter = moshi.adapter(clazz);
        List<T> elements = new ArrayList<>();
        removeInvalidElementsList(adapter, reader, elements);
        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Moshi moshi,
            JsonReader reader,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, moshi, reader);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
