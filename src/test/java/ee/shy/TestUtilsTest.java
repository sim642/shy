package ee.shy;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestUtilsTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testUtilsClass() throws Exception {
        TestUtils.assertUtilityClass(TestUtils.class);
    }

    private static class NonFinalUtils {

    }

    @Test
    public void testNonFinal() throws Exception {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("final");

        TestUtils.assertUtilityClass(NonFinalUtils.class);
    }

    private static final class PublicConstructorUtils {
        public PublicConstructorUtils() {

        }
    }

    @Test
    public void testPublicConstructor() throws Exception {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("constructor");
        expectedException.expectMessage("private");

        TestUtils.assertUtilityClass(PublicConstructorUtils.class);
    }

    private static final class MultiConstructorUtils {
        private MultiConstructorUtils() {

        }

        @SuppressWarnings("UnusedParameters")
        private MultiConstructorUtils(boolean placeholder) {

        }
    }

    @Test
    public void testMultiConstructor() throws Exception {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("constructor");
        expectedException.expectMessage("one");

        TestUtils.assertUtilityClass(MultiConstructorUtils.class);
    }

    private static final class NonStaticUtils {
        private NonStaticUtils() {

        }

        @SuppressWarnings({"EmptyMethod", "unused"})
        public void placeholder() {

        }
    }

    @Test
    public void testNonStatic() throws Exception {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("exists");
        expectedException.expectMessage("non-static");
        expectedException.expectMessage("method");

        TestUtils.assertUtilityClass(NonStaticUtils.class);
    }
}