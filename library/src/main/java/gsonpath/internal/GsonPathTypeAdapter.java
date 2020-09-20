package gsonpath.internal;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import static gsonpath.internal.GsonUtil.isValidValue;

public abstract class GsonPathTypeAdapter<T> extends JsonAdapter<T> {
    protected final Moshi moshi;

    public GsonPathTypeAdapter(Moshi moshi) {
        this.moshi = moshi;
    }

    @Override
    public final T fromJson(JsonReader reader) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(reader)) {
            return null;
        }
        return readImpl(reader);
    }

    @Override
    public final void toJson(JsonWriter writer, T value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        writeImpl(writer, value);
    }

    public abstract T readImpl(JsonReader reader) throws IOException;

    public abstract void writeImpl(JsonWriter writer, T value) throws IOException;
}
