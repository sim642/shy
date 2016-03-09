package shy.storage;

import java.io.InputStream;

public interface DataStorage {
    String add(InputStream source);
    InputStream get(String hash);
}
