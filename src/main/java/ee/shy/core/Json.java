package ee.shy.core;

import com.google.gson.*;
import ee.shy.storage.Hash;

import java.io.*;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Json {
    public static Gson gson;
    private Commit commit;

    Json() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Hash.class, new HashSerializer());
        gsonBuilder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer());
        gson = gsonBuilder.create();
    }

    public Gson getGson() {
        return gson;
    }

    public void readCommitJson() throws IOException{
        try (Reader reader = new InputStreamReader(new FileInputStream("commitTest.json"), "UTF-8")) {
            commit = gson.fromJson(reader, Commit.class);
            reader.close();
        }
    }

    public void writeCommitJson() throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("oJsonTest.json"), "UTF-8")) {
            gson.toJson(commit, writer);
            writer.close();
        }
    }

    private class HashSerializer implements JsonSerializer<Hash>, JsonDeserializer<Hash> {
        @Override
        public JsonElement serialize(Hash hash, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(hash.toString());
        }
        @Override
        public Hash deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Hash(jsonElement.getAsJsonPrimitive().getAsString());
        }
    }

    private class OffsetDateTimeSerializer implements JsonSerializer<OffsetDateTime> , JsonDeserializer<OffsetDateTime> {
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
