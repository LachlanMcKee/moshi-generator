package gsonpath.safe;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import gsonpath.GsonPathTypeAdapterFactory;
import gsonpath.GsonSafeList;
import okio.Okio;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class GsonSafeListTest {
    @Test
    public void testUsingGsonSafeArrayList() throws IOException {
        Moshi moshi = new Moshi.Builder()
                .add(new GsonPathTypeAdapterFactory())
                .build();

        InputStream resourceAsStream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream("sample.json");

        GsonSafeList<Integer> typesList = moshi.<GsonSafeList<Integer>>adapter(Types.newParameterizedType(GsonSafeList.class, Integer.class))
                .fromJson(Okio.buffer(Okio.source(resourceAsStream)));

        assertEquals(2, typesList.size());
        assertEquals(new Integer(1), typesList.get(0));
        assertEquals(new Integer(4), typesList.get(1));
    }
}
