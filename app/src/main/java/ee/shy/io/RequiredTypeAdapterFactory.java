package ee.shy.io;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Gson type adapter factory for creating {@link Required} field checking type adapters.
 */
/*
    Based on:
    http://stackoverflow.com/a/35688340/854540
    http://stackoverflow.com/a/21634867/854540
 */
public class RequiredTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        final Class<? super T> classofT = type.getRawType();
        final Field[] requiredFields = Arrays.stream(classofT.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Required.class))
                .toArray(Field[]::new);

        if (requiredFields.length == 0)
            return null; // only produce type adapters for types which have required fields

        for (Field requiredField : requiredFields) {
            requiredField.setAccessible(true); // make field checkable
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T object) throws IOException {
                checkRequiredFields(object, requiredFields);
                delegate.write(out, object);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T object = delegate.read(in);
                checkRequiredFields(object, requiredFields);
                return object;
            }
        };
    }

    /**
     * Checks if all required fields have a value.
     * @param object object which's fields to check
     * @param requiredFields array of fields to check
     * @param <T> type of object
     * @throws JsonParseException if object is missing a required field value
     */
    private static <T> void checkRequiredFields(T object, Field[] requiredFields) {
        try {
            for (Field requiredField : requiredFields) {
                Object value = requiredField.get(object);
                if (value == null)
                    throw new IllegalJsonException("missing required field: " + requiredField.getName());
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
