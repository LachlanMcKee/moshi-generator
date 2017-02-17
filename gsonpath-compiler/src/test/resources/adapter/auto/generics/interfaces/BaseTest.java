package adapter.auto.generics.interfaces;

import java.util.List;
import java.util.Map;

interface BaseTest<GENERIC_1, GENERIC_2, GENERIC_3> {
    GENERIC_1 getValue1();

    Map<GENERIC_2, GENERIC_3> getValue2();
}