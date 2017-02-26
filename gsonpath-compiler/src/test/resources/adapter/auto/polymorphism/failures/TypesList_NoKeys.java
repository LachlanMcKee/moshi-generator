package adapter.auto.polymorphism.failures;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_NoKeys {
    @GsonSubtype(
            fieldName = "type"
    )
    Type[] items;
}
