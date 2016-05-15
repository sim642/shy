package ee.shy.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreePathTest {
    @Test
    public void testResolve() throws Exception {
        TreePath path1 = new TreePath().resolve("foo");
        TreePath path2 = new TreePath().resolve("foo").resolve("bar");

        assertEquals(path1.resolve("bar").toString(), path2.toString());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("/", new TreePath().toString());
        assertEquals("/foo", new TreePath().resolve("foo").toString());
        assertEquals("/foo/bar/baz.txt", new TreePath().resolve("foo/bar").resolve("baz.txt").toString());
    }

    @Test
    public void testCompareTo() throws Exception {
        assertEquals(0, new TreePath().compareTo(new TreePath()));
        assertEquals(0, new TreePath().resolve("foo").compareTo(new TreePath().resolve("foo")));

        assertTrue(new TreePath().compareTo(new TreePath().resolve("foo")) < 0);
        assertTrue(new TreePath().resolve("foo").resolve("bar").compareTo(new TreePath().resolve("foo").resolve("baz").resolve("shz")) < 0);
        assertTrue(new TreePath().resolve("foo").resolve("bar").compareTo(new TreePath().resolve("foo")) > 0);
    }
}