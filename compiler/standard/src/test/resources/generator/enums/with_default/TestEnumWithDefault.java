package generator.enums.with_default;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.EnumGsonAdapter;

@EnumGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
enum TestEnumWithDefault {
    @EnumGsonAdapter.DefaultValue
    VALUE_ABC,
    VALUE_DEF,
    @SerializedName("custom")
    VALUE_GHI,
    VALUE_1
}