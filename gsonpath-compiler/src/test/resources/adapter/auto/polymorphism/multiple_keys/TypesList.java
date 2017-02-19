package adapter.auto.polymorphism.multiple_keys;

import adapter.auto.polymorphism.Type1;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "type",
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = Type1.class)
            },
            integerKeys = {
                    @GsonSubtype.IntegerKey(key = 1, subtype = Type1.class)
            }
    )
    Type[] items;
}
