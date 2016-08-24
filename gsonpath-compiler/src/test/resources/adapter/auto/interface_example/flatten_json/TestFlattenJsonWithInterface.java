package adapter.auto.interface_example.flatten_json;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;
import gsonpath.FlattenJson;

@AutoGsonAdapter
public interface TestFlattenJsonWithInterface {
    @FlattenJson
    String getFlattenExample();
}