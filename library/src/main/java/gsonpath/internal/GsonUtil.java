package gsonpath.internal;

import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

/**
 * A set of Gson utilities which expose functionality to read content from a JsonReader
 * safely without throwing IOExceptions.
 */
public class GsonUtil {

    private GsonUtil() {
    }

    /**
     * Determines whether the next value within the reader is not null.
     *
     * @param reader the json reader used to read the stream
     * @return true if a valid value exists, or false if the value is null.
     * @throws IOException see {@link JsonReader#skipValue()}
     */
    public static boolean isValidValue(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.skipValue();
            return false;
        }
        return true;
    }

    public static <T> void writeWithGenericAdapter(Moshi moshi, Class<T> clazz, JsonWriter writer, T obj0) throws IOException {
        moshi.adapter(clazz).toJson(writer, obj0);
    }
}
