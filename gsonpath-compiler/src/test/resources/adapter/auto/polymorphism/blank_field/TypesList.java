package adapter.auto.polymorphism.blank_field;

import adapter.auto.polymorphism.Type1;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "",
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = Type1.class)
            }
    )
    Type[] items;
}
