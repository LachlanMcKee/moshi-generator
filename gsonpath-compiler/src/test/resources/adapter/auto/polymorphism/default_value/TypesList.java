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
            subTypeKey = "type",
            subTypeFailureOutcome = GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE,
            defaultType = Type2.class,
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class)
            }
    )
    Type[] items;
}
