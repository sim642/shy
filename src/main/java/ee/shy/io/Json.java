package ee.shy.io;

import com.google.gson.*;
import ee.shy.storage.Hash;

import java.io.*;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class providing general JSON serialization/deserialization via gson.
 */
public class Json {
    /**
     * gson object used for all operations.
     */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Hash.class, new HashBiserializer())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeBiserializer())
            .create();

    /**
     * Writes an object to an output stream as JSON.
     * @param os output stream to write to
     * @param object object to write
     * @param <T> type of writable object
     * @throws IOException
     */
    public static <T> void write(OutputStream os, T object) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, "UTF-8")) {
            gson.toJson(object, writer);
        }
    }

    /**
     * Reads an object from an input stream as JSON.
     * @param is input stream to read from
     * @param classofT class of object to read
     * @param <T> type of readable object
     * @return object read
     * @throws IOException
     */
    public static <T> T read(InputStream is, Class<T> classofT) throws IOException {
        try (Reader reader = new InputStreamReader(is, "UTF-8")) {
            return gson.fromJson(reader, classofT);
        }
    }

    /**
     * Serializer and deserializer for {@link Hash} objects.
     */
    private static class HashBiserializer implements JsonBiserializer<Hash> {
        @Override
        public JsonElement serialize(Hash hash, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(hash.toString());
        }
        @Override
        public Hash deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Hash(jsonElement.getAsString());
        }
    }

    /**
     * Serializer and deserializer for {@link OffsetDateTime} objects in ISO 8601 format.
     */
    private static class OffsetDateTimeBiserializer implements JsonBiserializer<OffsetDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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