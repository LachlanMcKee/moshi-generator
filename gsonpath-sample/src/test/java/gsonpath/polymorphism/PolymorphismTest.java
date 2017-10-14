package gsonpath.polymorphism;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class PolymorphismTest {

    @Test
    public void testValidPolymorphism() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .create();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_valid.json");

        TypesList typesList = gson.fromJson(new InputStreamReader(resourceAsStream), TypesList.class);
        Type[] types = typesList.getItems();
        Assert.assertEquals(types.length, 5);

        Type1 value1 = (Type1) types[0];
        Assert.assertEquals(value1.name, "Type1 Example 1");
        Assert.assertEquals(value1.intTest, 1);

        Type1 value2 = (Type1) types[1];
        Assert.assertEquals(value2.name, "Type1 Example 2");
        Assert.assertEquals(value2.intTest, 2);

        Type2 value3 = (Type2) types[2];
        Assert.assertEquals(value3.name, "Type2 Example 1");
        Assert.assertEquals(value3.doubleTest, 1.0, 0);

        Type3 value4 = (Type3) types[3];
        Assert.assertEquals(value4.name, "Type3 Example 1");
        Assert.assertEquals(value4.stringTest, "123");

        Assert.assertNull(types[4]);
    }

    @Test
    public void givenNullTypeField_whenReadingJson_thenExpectException() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .create();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_type_is_null.json");

        try {
            gson.fromJson(new InputStreamReader(resourceAsStream), TypesList.class);

        } catch (JsonParseException e) {
            // Since the mandatory value is not found, we are expecting an exception.
            Assert.assertEquals(JsonParseException.class, e.getClass());
            Assert.assertEquals("cannot deserialize gsonpath.polymorphism.Type because the subtype field 'type' is either null or does not exist.", e.getMessage());
            return;
        }

        Assert.fail("Expected JsonParseException was not triggered");
    }

}
