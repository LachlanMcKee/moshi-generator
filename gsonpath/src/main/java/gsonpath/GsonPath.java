package gsonpath;

import gsonpath.internal.GsonPathTypeAdapterFactory;

/**
 * The primary class to use when using the GsonPath library.
 * <p>
 * It supplies factories which expose auto generated class created using the {@link gsonpath.AutoGsonAdapter} annotation.
 */
public class GsonPath {
    /**
     * Creates an instance of the {@link gsonpath.internal.GsonPathTypeAdapterFactory} class.
     * <p>
     * This factory is used to map the auto generated {@link com.google.gson.TypeAdapter} classes created using the
     * {@link gsonpath.AutoGsonAdapter} annotation.
     * <p>
     * Only a single use of reflection is used within the constructor, so it isn't critical to hold onto this reference
     * for later usage.
     *
     * @return a new instance of the {@link gsonpath.internal.GsonPathTypeAdapterFactory} class
     */
    public static GsonPathTypeAdapterFactory createTypeAdapterFactory() {
        return new GsonPathTypeAdapterFactory();
    }
}
