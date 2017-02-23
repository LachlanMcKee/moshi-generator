package adapter.auto.polymorphism.default_value;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

import adapter.auto.polymorphism.Type;
import adapter.auto.polymorphism.Type1;
import adapter.auto.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            fieldName = "type",
            subTypeFailureOutcome = GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE,
            defaultType = Type2.class,
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = Type1.class)
            }
    )
    Type[] items;
}
