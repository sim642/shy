package shy.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory data storage class using a {@link HashMap}.
 */
public class MapStorage extends DataStorage {
    /**
     * Hashmap to use for storing all data by hash.
     */
    private final Map<String, byte[]> storage;

    /**
     * Constructs an empty map storage class.
     */
    public MapStorage() {
        storage = new HashMap<>();
    }

    @Override
    protected void add(Hash hash, InputStream source) throws IOException {
        storage.put(hash.toString(), Util.toByteArray(source));
    }

    @Override
    public InputStream get(Hash hash) {
        byte[] buffer = storage.get(hash.toString());
        if (buffer != null)
            return new ByteArrayInputStream(buffer);
        else
            return null;
    }
}
