package adapter.auto.polymorphism.integer_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import adapter.auto.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            integerValueSubtypes = {
                    @GsonSubtype.IntegerValueSubtype(value = 0, subtype = Type1.class),
                    @GsonSubtype.IntegerValueSubtype(value = 1, subtype = Type2.class)
            }
    )
    Type[] items;
}
