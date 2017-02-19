package adapter.auto.polymorphism.no_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "type"
    )
    Type[] items;
}
