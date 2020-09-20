package gsonpath.array;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ArrayExampleTest {
    private static final String ARRAY_VALUE = "{\"test1\":[null,1],\"test2\":[null,null,{\"child\":10,\"child2\":20}],\"test3\":[null,null,null,{\"child\":[null,50]}],\"test4\":{\"child\":[null,100]}}";

    @Test
    public void testSerialize() {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));
        Moshi moshi = builder.build();

        ArrayExample model = new ArrayExample();
        model.plainArray = 1;
        model.arrayWithNestedObject = 10;
        model.arrayWithNestedObject2 = 20;
        model.arrayWithNestedArray = 50;
        model.objectWithNestedArray = 100;

        String test = moshi.adapter(ArrayExample.class).toJson(model);
        Assert.assertEquals(ARRAY_VALUE, test);
    }

    @Test
    public void testDeserialize() throws IOException {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));
        Moshi moshi = builder.build();

        ArrayExample vanillaModel = moshi.adapter(ArrayExample.class).fromJson(ARRAY_VALUE);

        Assert.assertEquals(1, vanillaModel.plainArray);
        Assert.assertEquals(10, vanillaModel.arrayWithNestedObject);
        Assert.assertEquals(20, vanillaModel.arrayWithNestedObject2);
        Assert.assertEquals(50, vanillaModel.arrayWithNestedArray);
        Assert.assertEquals(100, vanillaModel.objectWithNestedArray);
    }
}
