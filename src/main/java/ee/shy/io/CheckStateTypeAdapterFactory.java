package ee.shy.io;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Gson type adapter factory for creating {@link CheckState} method invoking type adapters.
 */
public class CheckStateTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        final Class<? super T> classofT = type.getRawType();
        Method[] checkMethods = Arrays.stream(classofT.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CheckState.class))
                .toArray(Method[]::new);

        if (checkMethods.length == 0)
            return null; // only produce type adapters for types which have state check methods

        for (Method checkMethod : checkMethods) {
            checkMethod.setAccessible(true); // make method checkable
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T object) throws IOException {
                invokeCheckMethods(object, checkMethods);
                delegate.write(out, object);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T object = delegate.read(in);
                invokeCheckMethods(object, checkMethods);
                return object;
            }
        };
    }

    /**
     * Invokes all check methods.
     * @param object object which's methods to invoke
     * @param checkMethods array of methods to invoke
     * @param <T> type of object
     * @throws IllegalJsonException if object check method throws a {@link IllegalStateException}
     */
    private static <T> void invokeCheckMethods(T object, Method[] checkMethods) {
        try {
            for (Method checkMethod : checkMethods) {
                checkMethod.invoke(object);
            }
        }
        catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IllegalStateException)
                throw new IllegalJsonException("illegal state", cause);
            else
                throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
