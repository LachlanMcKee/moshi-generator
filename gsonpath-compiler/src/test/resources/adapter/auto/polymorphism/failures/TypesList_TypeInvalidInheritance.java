package adapter.auto.polymorphism.failures;

import java.lang.String;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_TypeInvalidInheritance {
    @GsonSubtype(
            fieldName = "type",
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = String.class)
            }
    )
    Type[] items;
}
