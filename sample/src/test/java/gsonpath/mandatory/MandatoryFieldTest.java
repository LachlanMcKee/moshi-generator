package gsonpath.mandatory;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import gsonpath.exception.JsonFieldNoKeyException;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class MandatoryFieldTest {
    @Test
    public void test() {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("MandatoryTestJson.json");

        Moshi moshi = builder.build();

        try {
            moshi.adapter(MandatorySampleModel.class).fromJson(Okio.buffer(Okio.source(resourceAsStream)));

        } catch (Exception e) {
            // Since the mandatory value is not found, we are expecting an exception.
            Assert.assertEquals(e.getClass(), JsonFieldNoKeyException.class);
            return;
        }

        Assert.fail("Expected JsonFieldMissingException was not triggered");
    }
}
