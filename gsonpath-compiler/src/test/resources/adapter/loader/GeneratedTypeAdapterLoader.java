package gsonpath;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import gsonpath.internal.TypeAdapterLoader;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;

public final class GeneratedTypeAdapterLoader implements TypeAdapterLoader {
    private final HashMap<String, TypeAdapterLoader> mPackagePrivateLoaders;

    public GeneratedTypeAdapterLoader() {
        mPackagePrivateLoaders = new HashMap<>();

        TypeAdapterLoader adapter_loader_Loader = new adapter.loader.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders.put("adapter.loader.TestLoaderSource", adapter_loader_Loader);

        TypeAdapterLoader adapter_loader_source3_Loader = new adapter.loader.source3.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders.put("adapter.loader.source3.TestLoaderSource", adapter_loader_source3_Loader);

        TypeAdapterLoader adapter_loader_source2_Loader = new adapter.loader.source2.PackagePrivateTypeAdapterLoader();
        mPackagePrivateLoaders.put("adapter.loader.source2.TestLoaderSource", adapter_loader_source2_Loader);
        mPackagePrivateLoaders.put("adapter.loader.source2.TestLoaderSource2", adapter_loader_source2_Loader);
    }

    @Override
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = type.getRawType();

        TypeAdapterLoader typeAdapterLoader = mPackagePrivateLoaders.get(rawType.getName());
        if (typeAdapterLoader != null) {
            return typeAdapterLoader.create(gson, type);
        }

        return null;
    }
}