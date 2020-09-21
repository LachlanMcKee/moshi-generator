package gsonpath.adapter.subType;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class GsonSubTypeTest {

    @Test
    public void testValidGsonSubTypeUsingList() throws IOException {
        Moshi moshi = new Moshi.Builder()
                .add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .build();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_valid.json");

        TypesList typesList = moshi.adapter(TypesList.class).fromJson(Okio.buffer(Okio.source(resourceAsStream)));
        Type[] types = typesList.getItems();
        Assert.assertEquals(6, types.length);

        validateValue1(types[0]);
        validateValue2(types[1]);
        validateValue3(types[2]);
        validateValue4(types[3]);
        validateValue5(types[4]);
        validateValue6(types[5]);
    }

    private void validateValue1(Type type) {
        Type.Type1 value1 = (Type.Type1) type;
        Assert.assertEquals("type1", value1.type);
        Assert.assertEquals("Type1 Example 1", value1.name);
        Assert.assertEquals(1, value1.intTest);
    }

    private void validateValue2(Type type) {
        Type.Type1 value2 = (Type.Type1) type;
        Assert.assertEquals("type1", value2.type);
        Assert.assertEquals("Type1 Example 2", value2.name);
        Assert.assertEquals(2, value2.intTest);
    }

    private void validateValue3(Type type) {
        Type.Type2 value3 = (Type.Type2) type;
        Assert.assertEquals("type2", value3.type);
        Assert.assertEquals("Type2 Example 1", value3.name);
        Assert.assertEquals(1.0, value3.doubleTest, 0);
    }

    private void validateValue4(Type type) {
        Type.Type3 value4 = (Type.Type3) type;
        Assert.assertEquals("type3", value4.type);
        Assert.assertEquals("Type3 Example 1", value4.name);
        Assert.assertEquals("123", value4.stringTest);
    }

    private void validateValue5(Type type) {
        Type.TypeNull value5 = (Type.TypeNull) type;
        Assert.assertNull(value5.type);
        Assert.assertEquals("TypeNull Example 1", value5.name);
    }

    private void validateValue6(Type type) {
        Assert.assertNull(type);
    }

    @Test
    public void testValidGsonSubTypeUsingPojo() throws IOException {
        Moshi moshi = new Moshi.Builder()
                .add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .build();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_valid.json");

        TypesPojo typesList = moshi.adapter(TypesPojo.class).fromJson(Okio.buffer(Okio.source(resourceAsStream)));

        validateValue1(typesList.getItem0());
        validateValue2(typesList.getItem1());
        validateValue3(typesList.getItem2());
        validateValue4(typesList.getItem3());
        validateValue5(typesList.getItem4());
    }

}
