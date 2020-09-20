package gsonpath;

import com.squareup.moshi.JsonAdapter;
import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.annotation.AutoGsonAdapterFactory;

/**
 * The primary class to use when using the GsonPath library.
 * <p>
 * It supplies factories which expose auto generated class created using the {@link AutoGsonAdapter} annotation.
 */
public class GsonPath {
    private static final String FACTORY_IMPLEMENTATION_SUFFIX = "Impl";

    private GsonPath() {
    }

    /**
     * Creates an instance of an {@link JsonAdapter.Factory} implementation class that implements the input interface.
     * <p>
     * This factory is used to map the auto generated {@link JsonAdapter} classes created using the
     * {@link AutoGsonAdapter} annotation.
     * <p>
     * Only a single use of reflection is used within the constructor, so it isn't critical to hold onto this reference
     * for later usage.
     *
     * @param clazz the type adatper class to use to find the concrete implementation. Ensure that the interface is
     *              annotated with {@link AutoGsonAdapterFactory}
     * @return a new instance of the {@link JsonAdapter.Factory} class
     */
    public static JsonAdapter.Factory createTypeAdapterFactory(Class<? extends JsonAdapter.Factory> clazz) {
        String factoryClassName = clazz.getCanonicalName() + FACTORY_IMPLEMENTATION_SUFFIX;
        try {
            return (JsonAdapter.Factory) Class.forName(factoryClassName).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to instantiate generated TypeAdapterFactory '" + factoryClassName + "'", e);
        }
    }
}
