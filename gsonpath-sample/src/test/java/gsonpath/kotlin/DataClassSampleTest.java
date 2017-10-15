package gsonpath.kotlin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class DataClassSampleTest {
    @Test
    public void test() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("DataClassJson.json");

        Gson gson = builder.create();
        DataClassSample model = gson.fromJson(new InputStreamReader(resourceAsStream), DataClassSample.class);

        Assert.assertEquals("test", model.getValue1());
        Assert.assertEquals(false, model.isBooleanTest1());
        Assert.assertEquals(null, model.isBooleanTest2());
    }
}
