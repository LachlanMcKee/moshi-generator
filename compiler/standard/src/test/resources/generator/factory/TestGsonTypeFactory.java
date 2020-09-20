package generator.factory;

import com.squareup.moshi.JsonAdapter;
import gsonpath.annotation.AutoGsonAdapterFactory;

@AutoGsonAdapterFactory
public interface TestGsonTypeFactory extends JsonAdapter.Factory {
}
