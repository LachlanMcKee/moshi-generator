package gsonpath.internal;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;

/**
 * An array type adapter that is strict about the type being used, it will only use the type adapter being wrapped
 * and will not attempt to find a more appropriate model, unlike the {@link com.google.gson.internal.bind.ArrayTypeAdapter}
 * that this class is based on.
 *
 * @param <E> the object type to deserialize.
 */
public final class StrictArrayTypeAdapter<E> extends JsonAdapter<Object> {
    private final Class<E> componentType;
    private final boolean filterNulls;
//    private final TypeAdapter<E> componentTypeAdapter;

    public StrictArrayTypeAdapter(JsonAdapter<E> componentTypeAdapter, Class<E> componentType, boolean filterNulls) {
//        this.componentTypeAdapter = componentTypeAdapter;
        this.componentType = componentType;
        this.filterNulls = filterNulls;
    }

    @Override
    public Object fromJson(JsonReader reader) throws IOException {
        return null;
    }

    @Override
    public void toJson(JsonWriter writer, Object value) throws IOException {

    }

//    @Override
//    public Object read(JsonReader reader) throws IOException {
//        if (reader.peek() == JsonToken.NULL) {
//            reader.nextNull();
//            return null;
//        }
//
//        List<E> list = new ArrayList<>();
//        reader.beginArray();
//        while (reader.hasNext()) {
//            E instance = componentTypeAdapter.read(reader);
//
//            if (filterNulls && instance == null) {
//                continue;
//            }
//
//            list.add(instance);
//        }
//        reader.endArray();
//        Object array = Array.newInstance(componentType, list.size());
//        for (int i = 0; i < list.size(); i++) {
//            Array.set(array, i, list.get(i));
//        }
//        return array;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void write(JsonWriter writer, Object array) throws IOException {
//        if (array == null) {
//            writer.nullValue();
//            return;
//        }
//
//        writer.beginArray();
//        for (int i = 0, length = Array.getLength(array); i < length; i++) {
//            E value = (E) Array.get(array, i);
//            componentTypeAdapter.write(writer, value);
//        }
//        writer.endArray();
//    }
}
