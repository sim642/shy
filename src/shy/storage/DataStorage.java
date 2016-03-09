package shy.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Map-like abstract storage class for binary data.
 * Access data by hash.
 *
 * @see Hash
 * @see MapStorage
 * @see FileStorage
 */
public abstract class DataStorage {
    /**
     * Adds binary data from input stream to storage.
     * @param source input stream to get data from
     * @return hash of stored data
     * @throws IOException
     */
    public final Hash add(InputStream source) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(source, md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Util.copyStream(dis, baos);

            Hash hash = new Hash(md);
            add(hash, new ByteArrayInputStream(baos.toByteArray()));
            return hash;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds binary data from input stream with precomputed hash.
     * @param hash hash of data from input stream
     * @param source input stream to get data from
     * @throws IOException
     */
    protected abstract void add(Hash hash, InputStream source) throws IOException;

    /**
     * Gets binary data as input stream by hash.
     * Input data is checked against given hash to ensure correctness.
     * @param hash hash of data to get
     * @return input stream of data
     * @throws IOException
     */
    public final InputStream get(Hash hash) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(getUnchecked(hash), md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Util.copyStream(dis, baos);

            Hash hashComputed = new Hash(md);
            if (!hash.equals(hashComputed))
                throw new RuntimeException("stored file content does not match hash");

            return new ByteArrayInputStream(baos.toByteArray());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets binary data as input stream by hash.
     * Input data is <b>not</b> checked against given hash to ensure correctness.
     * @param hash hash of data to get
     * @return input stream of data
     * @throws IOException
     */
    public abstract InputStream getUnchecked(Hash hash) throws IOException;
}
