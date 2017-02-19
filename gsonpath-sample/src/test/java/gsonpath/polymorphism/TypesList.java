package gsonpath.polymorphism;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
interface TypesList {
    @GsonSubtype(
            fieldName = "type",
            stringKeys = {
                    @GsonSubtype.StringKey(key = "type1", subtype = Type1.class),
                    @GsonSubtype.StringKey(key = "type2", subtype = Type2.class),
                    @GsonSubtype.StringKey(key = "type3", subtype = Type3.class)
            }
    )
    Type[] getItems();
}
