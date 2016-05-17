package ee.shy.core.merge;

import java.io.IOException;
import java.nio.file.Path;

public interface Merger<T> {
    void merge(Path path, T original, T revised) throws IOException;
}
