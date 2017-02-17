package adapter.auto.generics.interfaces;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface GenericsTest extends IntermediateTest<String, Double> {
}