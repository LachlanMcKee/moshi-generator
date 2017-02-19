package adapter.auto.polymorphism.type_no_inheritance;

import java.lang.String;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "type",
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = String.class)
            }
    )
    Type[] items;
}
