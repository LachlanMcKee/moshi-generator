package adapter.auto.polymorphism.string_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import adapter.auto.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "type",
            integerKeys = {
                    @GsonSubtype.IntegerKey(key = 0, subtype = Type1.class),
                    @GsonSubtype.IntegerKey(key = 1, subtype = Type2.class)
            }
    )
    Type[] items;
}
