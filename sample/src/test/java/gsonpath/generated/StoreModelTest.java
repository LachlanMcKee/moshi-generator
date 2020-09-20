package gsonpath.generated;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class StoreModelTest {
    @Test
    public void test() throws IOException {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("BookJson.json");

        Moshi gson = builder.build();
        StoreModel model = gson.adapter(StoreModel.class).fromJson(Okio.buffer(Okio.source(resourceAsStream)));

        Assert.assertEquals("red", model.bikeColour);
        Assert.assertEquals(4, model.bookList.size());
        Assert.assertEquals("J. R. R. Tolkien", model.bookList.get(3).author);
    }
}
