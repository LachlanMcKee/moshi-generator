package adapter.auto.polymorphism.integer_keys;

import static gsonpath.GsonUtil.*;

import adapter.auto.polymorphism.Type;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.internal.StrictArrayTypeAdapter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.util.Map;

public final class TypesList_GsonTypeAdapter extends TypeAdapter<TypesList> {
    private final Gson mGson;

    private StrictArrayTypeAdapter itemsGsonSubtype;

    public TypesList_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TypesList read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TypesList result = new TypesList();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "items":
                    jsonFieldCounter0++;

                    adapter.auto.polymorphism.Type[] value_items = (adapter.auto.polymorphism.Type[]) getItemsGsonSubtype().read(in);
                    if (value_items != null) {
                        result.items = value_items;
                    }
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return result;
    }

    @Override
    public void write(JsonWriter out, TypesList value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Type[] obj0 = value.items;
        if (obj0 != null) {
            out.name("items");
            getItemsGsonSubtype().write(out, obj0);
        }

        // End
        out.endObject();
    }

    private StrictArrayTypeAdapter getItemsGsonSubtype() {
        if (itemsGsonSubtype == null) {
            itemsGsonSubtype = new StrictArrayTypeAdapter<>(new ItemsGsonSubtype(mGson), Type.class);
        }
        return itemsGsonSubtype;
    }

    private static final class ItemsGsonSubtype extends TypeAdapter<Type> {
        private final Map<Integer, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

        private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

        private ItemsGsonSubtype(Gson gson) {
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put(0, gson.getAdapter(adapter.auto.polymorphism.Type1.class));
            typeAdaptersDelegatedByClassMap.put(adapter.auto.polymorphism.Type1.class, gson.getAdapter(adapter.auto.polymorphism.Type1.class));

            typeAdaptersDelegatedByValueMap.put(1, gson.getAdapter(adapter.auto.polymorphism.Type2.class));
            typeAdaptersDelegatedByClassMap.put(adapter.auto.polymorphism.Type2.class, gson.getAdapter(adapter.auto.polymorphism.Type2.class));
        }

        @Override
        public Type read(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().remove("type");
            if (typeValueJsonElement == null) {
                throw new JsonParseException("cannot deserialize adapter.auto.polymorphism.Type because it does not define a field named 'type'");
            }
            int value = typeValueJsonElement.getAsInt();
            TypeAdapter<? extends adapter.auto.polymorphism.Type> delegate = typeAdaptersDelegatedByValueMap.get(value);
            if (delegate == null) {
                return null;
            }
            return delegate.fromJsonTree(jsonElement);
        }

        @Override
        public void write(JsonWriter out, Type value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
            delegate.write(out, value);
        }
    }
}