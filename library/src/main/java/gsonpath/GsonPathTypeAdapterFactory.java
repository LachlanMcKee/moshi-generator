package gsonpath;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import gsonpath.internal.GsonSafeListTypeAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Adds serialization/deserialization handling for GsonPath specific types to Gson.
 */
public final class GsonPathTypeAdapterFactory implements JsonAdapter.Factory {
    private final GsonSafeListTypeAdapterFactory safeListFactory =
            new GsonSafeListTypeAdapterFactory();

    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        return safeListFactory.create(type, annotations, moshi);
    }
}
