package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestSerializedNameAlternateUsedError {
    @SerializedName(value = "foo", alternate = "bar")
    public int element1;
}