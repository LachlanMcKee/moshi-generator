package generator.factory.source2;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.Class;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public final class PackagePrivateTypeAdapterLoader implements JsonAdapter.Factory {
    @Override
    public JsonAdapter create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        Class rawType = Types.getRawType(type);
        if (rawType.equals(TestLoaderSource.class)) {
            return new TestLoaderSource_GsonTypeAdapter(moshi);

        } else if (rawType.equals(TestLoaderSource2.class)) {
            return new TestLoaderSource2_GsonTypeAdapter(moshi);
        }

        return null;
    }
}
