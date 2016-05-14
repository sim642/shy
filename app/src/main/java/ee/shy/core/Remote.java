package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Validated;

import java.net.URI;
import java.util.Objects;

/**
 * Class representing a remote repository's URI.
 */
public class Remote implements Jsonable, Validated {
    /**
     * Repository's URI.
     */
    private final URI remote;

    /**
     * Constructs a remote with given URI.
     * @param remote remote URI for repository
     */
    public Remote(URI remote) {
        this.remote = remote;
        assertValid();
    }

    public URI getURI() {
        return this.remote;
    }

    @Override
    public void assertValid() {
        Objects.requireNonNull(remote, "repository has no URI");
    }
}