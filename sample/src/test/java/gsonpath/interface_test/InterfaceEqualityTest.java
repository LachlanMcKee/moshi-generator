package gsonpath.interface_test;

import com.squareup.moshi.Moshi;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class InterfaceEqualityTest {
    @Test
    public void onAssertEquals_givenSameJson_expectEqual() throws IOException {
        compareJsonFiles("InterfaceEqualityVariant1TestJson.json", "InterfaceEqualityVariant1TestJson.json", true);
    }

    @Test
    public void onAssertEquals_givenDifferentJson_expectNotEqual() throws IOException {
        compareJsonFiles("InterfaceEqualityVariant1TestJson.json", "InterfaceEqualityVariant2TestJson.json", false);
    }

    @Test
    public void givenModelCreated_whenToJsonInvoked_thenExpectJsonTextOutput() {
        Moshi moshi = buildMoshi();

        InterfaceExample interfaceExample = InterfaceExample.create(
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        String jsonText = moshi.adapter(InterfaceExample.class).toJson(interfaceExample);
        Assert.assertEquals("Check JSON text exists", "{\"intExample\":1}", jsonText);
    }

    private void compareJsonFiles(String filename1, String filename2, boolean expectEqual) throws IOException {
        Moshi moshi = buildMoshi();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InterfaceExample sample1 = moshi.adapter(InterfaceExample.class).fromJson(Okio.buffer(Okio.source(classLoader.getResourceAsStream(filename1))));
        InterfaceExample sample2 = moshi.adapter(InterfaceExample.class).fromJson(Okio.buffer(Okio.source(classLoader.getResourceAsStream(filename2))));

        if (expectEqual) {
            Assert.assertEquals(sample1, sample2);
        } else {
            Assert.assertNotEquals(sample1, sample2);
        }
    }

    private Moshi buildMoshi() {
        return new Moshi.Builder()
                .add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .build();
    }

}
