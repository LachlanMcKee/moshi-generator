package gsonpath.polymorphism;

import gsonpath.AutoGsonAdapter;
import gsonpath.internal.GsonPathElementList;

import java.util.List;

@AutoGsonAdapter
class TypesList extends GsonPathElementList<Type> {
    List<Type> items;

    @Override
    protected List<Type> getList() {
        return items;
    }
}
