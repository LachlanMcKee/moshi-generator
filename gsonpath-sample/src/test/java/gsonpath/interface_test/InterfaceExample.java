package gsonpath.interface_test;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface InterfaceExample {
    Integer getIntExample();

    Long getLongExample();

    Double getDoubleExample();

    Boolean getBooleanExample();

    Integer[] getIntArrayExample();

    Long[] getLongArrayExample();

    Double[] getDoubleArrayExample();

    Boolean[] getBooleanArrayExample();
}
