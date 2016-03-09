package shy.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileStorage extends DataStorage {
    private final List<FileLocator> locators;
    private final FileAccessor accessor;

    public FileStorage(List<FileLocator> locators, FileAccessor accessor) {
        this.locators = locators;
        this.accessor = accessor;
    }

    @Override
    protected void add(Hash hash, InputStream source) throws IOException {
        accessor.add(locators.get(0).locateAdd(hash), source); // always use first locator for add
    }

    @Override
    public InputStream get(Hash hash) throws IOException {
        for (FileLocator locator : locators) {
            InputStream source = accessor.get(locator.locateGet(hash));
            if (source != null)
                return source;
        }
        return null;
    }
}
