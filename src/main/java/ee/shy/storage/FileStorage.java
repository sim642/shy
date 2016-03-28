package ee.shy.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Filesystem based data storage class.
 */
public class FileStorage extends DataStorage {
    /**
     * List of file locators to use.
     */
    private final List<FileLocator> locators;

    /**
     * File accessor to use.
     */
    private final FileAccessor accessor;

    /**
     * Constructs a new filesystem storage class.
     * @param locators list of locators to use
     * @param accessor accessor to use
     */
    public FileStorage(List<FileLocator> locators, FileAccessor accessor) {
        this.locators = locators;
        this.accessor = accessor;
    }

    @Override
    protected void put(Hash hash, InputStream source) throws IOException {
        accessor.add(locators.get(0).locateAdd(hash), source); // always use first locator for add
    }

    @Override
    public InputStream getUnchecked(Hash hash) throws IOException {
        for (FileLocator locator : locators) {
            InputStream source = accessor.get(locator.locateGet(hash));
            if (source != null)
                return source;
        }
        return null;
    }
}
