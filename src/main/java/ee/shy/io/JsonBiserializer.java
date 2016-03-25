package ee.shy.io;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * Interface representing custom serializer and deserializer for Json.
 * @param <T> type for which the biserializer is being registered
 */
public interface JsonBiserializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {

}
