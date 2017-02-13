package gsonpath.generated;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StoreModelTest {
    @Test
    public void test() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("BookJson.json");

        Gson gson = builder.create();
        StoreModel model = gson.fromJson(new InputStreamReader(resourceAsStream), StoreModel.class);

        Assert.assertEquals(model.bikeColour, "red");
        Assert.assertEquals(model.bookList.size(), 4);
        Assert.assertEquals(model.bookList.get(3).author, "J. R. R. Tolkien");
    }
}
