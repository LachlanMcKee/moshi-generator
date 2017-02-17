package adapter.auto.generics.interfaces;

interface IntermediateTest<T, V> extends BaseTest<T, String, Integer> {
    V getValue3();
}