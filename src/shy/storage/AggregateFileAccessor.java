package shy.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AggregateFileAccessor implements FileAccessor {
    private final List<FileAccessor> accessors;

    public AggregateFileAccessor(List<FileAccessor> accessors) {
        this.accessors = accessors;
    }

    @Override
    public void add(File file, InputStream source) throws IOException {
        accessors.get(0).add(file, source); // always use first accessor for add
    }

    @Override
    public InputStream get(File file) throws IOException {
        InputStream source;
        for (FileAccessor accessor : accessors) {
            if ((source = accessor.get(file)) != null)
                return source;
        }
        return null;
    }
}
