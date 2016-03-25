package ee.shy.core;

import com.google.gson.*;
import ee.shy.storage.Hash;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Json {
    public static final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    public static final Gson gson = gsonBuilder.create();
    private Commit commit;

    Json() {
        gsonBuilder.registerTypeAdapter(Hash.class, new HashSerializer());
        gsonBuilder.registerTypeAdapter(Hash.class, new HashDeserializer());
        gsonBuilder.registerTypeAdapter(Hash.class, new MyListInstanceCreator());
    }

    public Gson getGson() {
        return gson;
    }

    public void writeCommitJson() throws IOException{
        try (Reader reader = new InputStreamReader(new FileInputStream("commitTest.json"), "UTF-8")) {
            commit = gson.fromJson(reader, Commit.class);
            reader.close();
        }
    }

    public void readCommitJson() throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("oJsonTest.json"), "UTF-8")) {
            gson.toJson(commit, writer);
            writer.close();
        }
    }

    private class HashSerializer implements JsonSerializer<Hash> {
        @Override
        public JsonElement serialize(Hash hash, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(hash.toString());
        }
    }
    private class HashDeserializer implements JsonDeserializer<Hash> {
        @Override
        public Hash deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Hash(jsonElement.getAsJsonObject().getAsString());
        }
    }

    private class ListSerialiser implements JsonSerializer<List<TreeItems>> {
        @Override
        public JsonElement serialize(List<TreeItems> treeItems, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(treeItems.toString());
        }
    }

    class MyListInstanceCreator implements InstanceCreator<ArrayList<?>> {
        @SuppressWarnings("unchecked")
        public ArrayList<?> createInstance(Type type) {
            return new ArrayList<>();
        }
    }
}
