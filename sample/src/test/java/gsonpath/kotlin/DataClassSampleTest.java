package gsonpath.kotlin;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import gsonpath.exception.JsonFieldNoKeyException;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class DataClassSampleTest {
    @Test
    public void testWithAllValues() throws IOException {
        DataClassSample model = runTest("DataClassJson_all_values.json");

        Assert.assertEquals("test", model.getValue1());
        Assert.assertFalse(model.isBooleanTest1());
        Assert.assertNull(model.isBooleanTest2());
    }

    @Test
    public void testWithMissingValues() {
        try {
            runTest("DataClassJson_missing_values.json");
            fail("Exception not triggered");
        } catch (Exception e) {
            Assert.assertEquals(JsonFieldNoKeyException.class, e.getClass());
            Assert.assertEquals("Mandatory JSON element 'parent.child.value1' was not found within class " +
                "'gsonpath.kotlin.DataClassSample'", e.getMessage());
        }
    }

    private DataClassSample runTest(String fileName) throws IOException {
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);

        Moshi moshi = builder.build();
        return moshi.adapter(DataClassSample.class).fromJson(Okio.buffer(Okio.source(resourceAsStream)));
    }
}
