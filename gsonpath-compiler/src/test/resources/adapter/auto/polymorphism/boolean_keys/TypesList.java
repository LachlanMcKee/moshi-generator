package adapter.auto.polymorphism.boolean_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import adapter.auto.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            booleanValueSubtypes = {
                    @GsonSubtype.BooleanValueSubtype(value = true, subtype = Type1.class),
                    @GsonSubtype.BooleanValueSubtype(value = false, subtype = Type2.class)
            }
    )
    Type[] items;
}
