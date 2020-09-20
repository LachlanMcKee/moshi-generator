package gsonpath.internal;

import com.squareup.moshi.*;
import gsonpath.GsonSafeList;
import gsonpath.extension.RemoveInvalidElementsUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import static gsonpath.internal.GsonUtil.isValidValue;

/**
 * A factory for the list that stores only valid results when parsing via Gson.
 * <p>
 * Any elements being deserialied that throw an exception are removed from the list.
 */
public final class GsonSafeListTypeAdapterFactory implements JsonAdapter.Factory {
    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        Class<?> rawType = Types.getRawType(type);
        if (GsonSafeList.class != rawType) {
            return null;
        }

        Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
        JsonAdapter<?> elementTypeAdapter = moshi.adapter(elementType);

        return new Adapter<>(elementTypeAdapter);
    }

    private static final class Adapter<E> extends JsonAdapter<GsonSafeList<E>> {
        private final JsonAdapter<E> elementJsonAdapter;

        Adapter(JsonAdapter<E> elementJsonAdapter) {
            this.elementJsonAdapter = elementJsonAdapter;
        }

        @Override
        public GsonSafeList<E> fromJson(JsonReader reader) throws IOException {
            if (!isValidValue(reader)) {
                return null;
            }

            GsonSafeList<E> collection = new GsonSafeList<>();
            RemoveInvalidElementsUtil.removeInvalidElementsList(elementJsonAdapter, reader, collection);
            return collection;
        }

        @Override
        public void toJson(JsonWriter writer, GsonSafeList<E> collection) throws IOException {
            throw new IllegalStateException("Writing is not supported by GsonSafeArrayList");
        }
    }
}
