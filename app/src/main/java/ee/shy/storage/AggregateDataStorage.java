package ee.shy.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Data storage to aggregate other data storages.
 * Allows simultaneous support for multiple storages.
 *
 * Useful for combining temporary in-memory data from {@link MapStorage} with on-disk data from {@link FileStorage}.
 */
public class AggregateDataStorage extends DataStorage {
    /**
     * List of storages to use.
     */
    private final List<DataStorage> storages;

    /**
     * Constructs a new aggregate data storage from given storages.
     * @param storages list of storages to use
     */
    public AggregateDataStorage(List<DataStorage> storages) {
        this.storages = storages;
    }

    @Override
    protected void put(Hash hash, InputStream source) throws IOException {
        storages.get(0).put(hash, source);
    }

    @Override
    protected InputStream getUnchecked(Hash hash) throws IOException {
        InputStream source;
        for (DataStorage storage : storages) {
            if ((source = storage.getUnchecked(hash)) != null)
                return source;
        }
        return null;
    }
}
