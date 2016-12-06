package adapter.auto.interface_example.valid;

import com.google.gson.annotations.SerializedName;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public final class TestValidInterface_GsonPathModel implements TestValidInterface {
    private final Integer value1;
    private final Integer value2;
    private final Integer value3;

    public TestValidInterface_GsonPathModel(Integer value1, Integer value2, Integer value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @Override
    @SerializedName("Json1.Nest1")
    public Integer getValue1() {
        return value1;
    }

    @Override
    public Integer getValue2() {
        return value2;
    }

    @Override
    @SerializedName("Json1.Nest3")
    public Integer getValue3() {
        return value3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestValidInterface_GsonPathModel that = (TestValidInterface_GsonPathModel) o;

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
        return "TestValidInterface{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                '}';
    }
}