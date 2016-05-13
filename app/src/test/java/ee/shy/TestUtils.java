package ee.shy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public final class TestUtils {
    private TestUtils() {

    }

    /**
     * Asserts that a utility class is well defined.
     * http://stackoverflow.com/a/10872497/854540
     * @param classofUtils class to assert
     */
    public static void assertUtilityClass(final Class<?> classofUtils)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        assertTrue("class must be final",
                Modifier.isFinal(classofUtils.getModifiers()));

        assertEquals("There must be only one constructor", 1,
                classofUtils.getDeclaredConstructors().length);
        final Constructor<?> constructor = classofUtils.getDeclaredConstructor();
        if (constructor.isAccessible() ||
                !Modifier.isPrivate(constructor.getModifiers())) {
            fail("constructor is not private");
        }
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);

        for (final Method method : classofUtils.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && method.getDeclaringClass().equals(classofUtils)) {
                fail("there exists a non-static method:" + method);
            }
        }
    }
}
