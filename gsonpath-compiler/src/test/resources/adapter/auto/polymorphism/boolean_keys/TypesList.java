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
            booleanKeys = {
                    @GsonSubtype.BooleanKey(key = true, subtype = Type1.class),
                    @GsonSubtype.BooleanKey(key = false, subtype = Type2.class)
            }
    )
    Type[] items;
}
