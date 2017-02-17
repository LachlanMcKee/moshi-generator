package adapter.auto.generics.interfaces;

import java.lang.Double;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public final class GenericsTest_GsonPathModel implements GenericsTest {
    private final String value1;

    private final Map<String, Integer> value2;

    private final Double value3;

    public GenericsTest_GsonPathModel(String value1, Map<String, Integer> value2, Double value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @Override
    public String getValue1() {
        return value1;
    }

    @Override
    public Map<String, Integer> getValue2() {
        return value2;
    }

    @Override
    public Double getValue3() {
        return value3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericsTest_GsonPathModel that = (GenericsTest_GsonPathModel) o;

        if (value1 != null ? !value1.equals(that.value1) : that.value1 != null) return false;
        if (value2 != null ? !value2.equals(that.value2) : that.value2 != null) return false;
        if (value3 != null ? !value3.equals(that.value3) : that.value3 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value1 != null ? value1.hashCode() : 0;
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GenericsTest{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                '}';
    }
}