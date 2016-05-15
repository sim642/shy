package ee.shy.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreePathTest {
    private static final TreePath EMPTY = new TreePath();
    private static final TreePath FOO = EMPTY.resolve("foo");
    private static final TreePath FOO_BAR = FOO.resolve("bar");

    @Test
    public void testResolve() throws Exception {
        assertEquals(FOO.resolve("bar").toString(), FOO_BAR.toString());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("/", EMPTY.toString());
        assertEquals("/foo", FOO.toString());
        assertEquals("/foo/bar", FOO_BAR.toString());
    }

    @Test
    public void startsWith() throws Exception {
        assertTrue(FOO.startsWith(EMPTY));
        assertTrue(FOO_BAR.startsWith(EMPTY));

        assertTrue(FOO_BAR.startsWith(FOO));
        assertFalse(FOO.startsWith(FOO_BAR));
    }

    @Test
    public void testCompareTo() throws Exception {
        assertEquals(0, EMPTY.compareTo(EMPTY));
        assertEquals(0, FOO.compareTo(FOO));

        assertTrue(EMPTY.compareTo(FOO) < 0);
        assertTrue(FOO_BAR.compareTo(FOO.resolve("baz").resolve("shz")) < 0);
        assertTrue(FOO_BAR.compareTo(FOO) > 0);
    }
}