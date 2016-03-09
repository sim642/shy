package shy.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends DataStorage {
    private final Map<String, byte[]> storage;

    public MapStorage() {
        storage = new HashMap<>();
    }

    @Override
    protected void add(String hash, InputStream source) throws IOException {
        storage.put(hash, Util.toByteArray(source));
    }

    @Override
    public InputStream get(String hash) {
        byte[] buffer = storage.get(hash);
        if (buffer != null)
            return new ByteArrayInputStream(buffer);
        else
            return null;
    }
}
