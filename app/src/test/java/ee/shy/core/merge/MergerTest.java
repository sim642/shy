package ee.shy.core.merge;

import ee.shy.TemporaryDirectory;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public abstract class MergerTest<T> {
    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    protected T patchable;
    protected T original;
    protected T revised;
    protected T patched;

    @Test
    public void testForwardMainMerge() throws Exception {
        testMerge(patchable, original, revised, patched);
    }

    @Test
    public void testForwardSideMerge() throws Exception {
        testMerge(revised, original, patchable, patched);
    }

    @Test
    public void testBackwardMainMerge() throws Exception {
        testMerge(patched, revised, original, patchable);
    }

    @Test
    public void testBackwardSideMerge() throws Exception {
        testMerge(original, revised, patched, patchable);
    }

    protected abstract void testMerge(T patchable, T original, T revised, T patched) throws IOException;
}
