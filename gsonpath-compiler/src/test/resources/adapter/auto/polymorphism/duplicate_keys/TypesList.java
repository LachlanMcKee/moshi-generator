package adapter.auto.polymorphism.duplicate_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import adapter.auto.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type2.class)
            }
    )
    Type[] items;
}
