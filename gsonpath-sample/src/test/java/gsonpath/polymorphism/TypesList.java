package gsonpath.polymorphism;

import gsonpath.AutoGsonAdapter;

import java.util.List;

@AutoGsonAdapter(rootField = "items")
interface TypesList extends List<Type> {
}
