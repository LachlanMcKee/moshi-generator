package adapter.auto.processor_errors;

import com.google.gson.annotations.SerializedName;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestDuplicateFieldError {
    @SerializedName("value")
    public double value1;

    @SerializedName("value")
    public double value2;
}