package ee.shy.io;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gson type adapter factory for asserting validity of {@link Validated} classes.
 */
public class ValidatedTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Validated.class.isAssignableFrom(type.getRawType())) // type does not implement interface Validated
            return null;

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                try {
                    ((Validated) value).assertValid();
                    delegate.write(out, value); // delegate chain may contain other Validated types for field classes
                }
                catch (IllegalStateException | IllegalArgumentException | NullPointerException e) {
                    // illegal objects should not exist in the first place but better safe than sorry
                    throw new IllegalJsonException(e);
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                try {
                    T value = delegate.read(in); // delegate chain may contain other Validated types for field classes
                    ((Validated) value).assertValid();
                    return value;
                }
                catch (IllegalStateException | IllegalArgumentException | NullPointerException e) {
                    throw new IllegalJsonException(e);
                }
            }
        };
    }
}
