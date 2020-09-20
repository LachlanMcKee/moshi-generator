package generator.factory;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.Override;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public final class TestGsonTypeFactoryImpl implements TestGsonTypeFactory {
    private final JsonAdapter.Factory[] mPackagePrivateLoaders;

    public TestGsonTypeFactoryImpl() {
        mPackagePrivateLoaders = new JsonAdapter.Factory[3];
        mPackagePrivateLoaders[0] = new generator.factory.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders[1] = new generator.factory.source2.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders[2] = new generator.factory.source3.PackagePrivateTypeAdapterLoader();
    }

    @Override
    public JsonAdapter create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        for (int i = 0; i < mPackagePrivateLoaders.length; i++) {
            JsonAdapter typeAdapter = mPackagePrivateLoaders[i].create(type, annotations, moshi);

            if (typeAdapter != null) {
                return typeAdapter;
            }
        }
        return null;
    }
}
