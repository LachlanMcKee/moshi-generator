package adapter.loader;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.Override;

public final class TestGsonTypeFactoryImpl implements TestGsonTypeFactory {
    private final TypeAdapterFactory[] mPackagePrivateLoaders;

    public GsonTypeAdapterLoader() {
        mPackagePrivateLoaders = new TypeAdapterFactory[3];
        mPackagePrivateLoaders[0] = new adapter.loader.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders[1] = new adapter.loader.source3.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders[2] = new adapter.loader.source2.PackagePrivateTypeAdapterLoader();
    }

    @Override
    public TypeAdapter create(Gson gson, TypeToken type) {
        for (int i = 0; i < mPackagePrivateLoaders.length; i++) {
            TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type);

            if (typeAdapter != null) {
                return typeAdapter;
            }
        }
        return null;
    }
}
