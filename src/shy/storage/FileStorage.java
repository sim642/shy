package shy.storage;

import java.io.IOException;
import java.io.InputStream;

public class FileStorage extends DataStorage {
    private final FileLocator locator;
    private final FileAccessor accessor;

    public FileStorage(FileLocator locator, FileAccessor accessor) {
        this.locator = locator;
        this.accessor = accessor;
    }

    @Override
    protected void add(Hash hash, InputStream source) throws IOException {
        accessor.add(locator.get(hash), source);
    }

    @Override
    public InputStream get(Hash hash) throws IOException {
        return accessor.get(locator.get(hash));
    }
}
