package ee.shy.core.merge;

import ee.shy.TemporaryDirectory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assume.*;

public abstract class MergerTest<T> {
    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    protected T patchable;
    protected T original;
    protected T revised;
    protected T patched;

    protected abstract T getState(String name) throws IOException;

    @Before
    public void setUp() throws Exception {
        patchable = getState("patchable");
        original = getState("original");
        revised = getState("revised");
        patched = getState("patched");
        assumeNotNull(patchable, original, revised, patchable);
    }

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
        testMerge(patchable, patched, revised, original);
    }

    @Test
    public void testBackwardSideMerge() throws Exception {
        testMerge(revised, patched, patchable, original);
    }

    protected abstract void testMerge(T patchable, T original, T revised, T patched) throws Exception;
}
