package ee.shy.core;

import com.google.gson.*;
import ee.shy.storage.Hash;

import java.io.*;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Json {
    public static Gson gson;
    private Commit commit;
    private Tree tree;

    Json() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Hash.class, new HashBiserializer());
        gsonBuilder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeBiserializer());
        gson = gsonBuilder.create();
    }

    public Gson getGson() {
        return gson;
    }

    public <T> void write(OutputStream os, T object) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, "UTF-8");) {
            gson.toJson(object, writer);
        }
    }
    public <T> T read(InputStream is, Class<T> classofT) throws IOException {
        try (Reader reader = new InputStreamReader(is, "UTF-8");) {
            return gson.fromJson(reader, classofT);
        }
    }

    private class HashBiserializer implements JsonBiserializer<Hash> {
        @Override
        public JsonElement serialize(Hash hash, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(hash.toString());
        }
        @Override
        public Hash deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Hash(jsonElement.getAsString());
        }
    }

    private class OffsetDateTimeBiserializer implements JsonBiserializer<OffsetDateTime> {
        private final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        @Override
        public JsonElement serialize(OffsetDateTime offsetDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(offsetDateTime.toString());
        }

        @Override
        public OffsetDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return FORMATTER.parse(jsonElement.getAsString(), OffsetDateTime::from);
        }
    }
}
