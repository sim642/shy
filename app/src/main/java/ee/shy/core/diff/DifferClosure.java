package ee.shy.core.diff;

import java.io.IOException;
import java.util.List;

/**
 * Closure class which applies original and revised arguments to a differ.
 * @param <T> type of objects being diffed
 */
public class DifferClosure<T> {
    private final Differ<T> differ;
    private final T original;
    private final T revised;

    public DifferClosure(Differ<T> differ, T original, T revised) {
        this.differ = differ;
        this.original = original;
        this.revised = revised;
    }

    /**
     * @see Differ#diff(Object, Object)
     */
    public List<String> diff() throws IOException {
        return differ.diff(original, revised);
    }

    /**
     * @see Differ#shortDiff(Object, Object)
     */
    public List<String> shortDiff() throws IOException {
        return differ.shortDiff(original, revised);
    }
}
