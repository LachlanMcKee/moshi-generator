package gsonpath.internal;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection type adapter that delegates and read and write functions to a provided type adapter.
 * The underlying collection type is a {@link List}.
 *
 * @param <E> the object type to serialize/deserialize.
 */
public final class CollectionTypeAdapter<E> extends JsonAdapter<Collection<E>> {
    private final JsonAdapter<E> componentTypeAdapter;
    private final boolean filterNulls;

    public CollectionTypeAdapter(JsonAdapter<E> componentTypeAdapter, boolean filterNulls) {
        this.componentTypeAdapter = componentTypeAdapter;
        this.filterNulls = filterNulls;
    }

    @Override
    public Collection<E> fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull();
            return null;
        }

        List<E> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            E instance = componentTypeAdapter.fromJson(reader);

            if (filterNulls && instance == null) {
                continue;
            }

            list.add(instance);
        }
        reader.endArray();
        return list;
    }

    @Override
    public void toJson(JsonWriter writer, Collection<E> list) throws IOException {
        if (list == null) {
            writer.nullValue();
            return;
        }

        writer.beginArray();
        for (E element : list) {
            componentTypeAdapter.toJson(writer, element);
        }
        writer.endArray();
    }
}
