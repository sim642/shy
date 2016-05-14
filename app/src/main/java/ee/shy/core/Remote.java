package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Validated;

import java.net.URI;
import java.util.Objects;

/**
 * Class representing a remote repository's.
 */
public class Remote implements Jsonable, Validated {
    /**
     * Repository's URI.
     */
    private final URI uri;

    /**
     * Constructs a remote with given URI.
     * @param uri URI for remote repository
     */
    public Remote(URI uri) {
        this.uri = uri;
        assertValid();
    }

    public URI getURI() {
        return this.uri;
    }

    @Override
    public void assertValid() {
        Objects.requireNonNull(uri, "remote has no URI");
    }
}