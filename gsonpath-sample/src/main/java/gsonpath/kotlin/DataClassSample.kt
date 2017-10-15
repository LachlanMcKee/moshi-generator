package gsonpath.kotlin

import com.google.gson.annotations.SerializedName
import gsonpath.AutoGsonAdapter

@AutoGsonAdapter
data class DataClassSample(
        @SerializedName("parent.child.")
        val value1: String,
        val isBooleanTest1: Boolean,
        val isBooleanTest2: Boolean?
)