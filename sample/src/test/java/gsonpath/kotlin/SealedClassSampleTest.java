package gsonpath.kotlin;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class SealedClassSampleTest {
    @Test
    public void testWithAllValues() throws IOException {
        SealedClassArray model = runTest(SealedClassArray.class, "SealedClassSample.json");

        Type item1 = model.getItems()[0];
        Assert.assertEquals("Type1 Example", item1.getName());
        Assert.assertEquals(1, ((Type.Type1) item1).getIntTest());

        Type item2 = model.getItems()[1];
        Assert.assertEquals("Type2 Example", item2.getName());
        Assert.assertEquals(1.0, ((Type.Type2) item2).getDoubleTest(), 0);

        Type item3 = model.getItems()[2];
        Assert.assertEquals("Type3 Example", item3.getName());
        Assert.assertEquals("123", ((Type.Type3) item3).getStringTest());
    }

    @Test
    public void testWrite() {
        testWrite(new SealedClassPojo("name", new Type.Type1("Foobar", 1), "other"),
                "{\"value1\":\"name\",\"item\":{\"common\":{\"name\":\"Foobar\"},\"specific\":{\"intTest\":1}},\"value2\":\"other\"}");

        testWrite(new SealedClassPojo("name", new Type.Type2("Foobar", 1.5d), "other"),
                "{\"value1\":\"name\",\"item\":{\"common\":{\"name\":\"Foobar\"},\"specific\":{\"doubleTest\":1.5}},\"value2\":\"other\"}");

        testWrite(new SealedClassPojo("name", new Type.Type3("Foobar", "Sample"), "other"),
                "{\"value1\":\"name\",\"item\":{\"common\":{\"name\":\"Foobar\"},\"specific\":{\"stringTest\":\"Sample\"}},\"value2\":\"other\"}");
    }

    private void testWrite(SealedClassPojo pojo, String json) {
        Assert.assertEquals(json, createMoshi().adapter(SealedClassPojo.class).toJson(pojo));
    }

    @Test
    public void testSinglePojo() throws IOException {
        SealedClassSubTypePojo model = runTest(SealedClassSubTypePojo.class, "SealedClassSample.json");

        Type item1 = model.getItem();
        Assert.assertEquals("Type1 Example", item1.getName());
        Assert.assertEquals(1, ((Type.Type1) item1).getIntTest());
    }

    @Test
    public void testAnnotatedSealedClass() throws IOException {
        Type model = runTest(Type.class, "SealedClassSample_SingleType.json");

        Assert.assertEquals("Type3 Example", model.getName());
        Assert.assertEquals("123", ((Type.Type3) model).getStringTest());
    }

    private <T> T runTest(Class<T> clazz, String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);

        return createMoshi().adapter(clazz).fromJson(Okio.buffer(Okio.source(resourceAsStream)));
    }

    private Moshi createMoshi() {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));
        return builder.build();
    }
}
