package generator.interf.valid;

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
        if (rawType.equals(TestValidInterface_GsonPathModel.class) || rawType.equals(TestValidInterface.class)) {
            return new TestValidInterface_GsonTypeAdapter(moshi);
        }

        return null;
    }
}
