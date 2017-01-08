package adapter.auto.interface_example.list;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoGsonAdapter
public interface TestListInterface extends List<String> {
}