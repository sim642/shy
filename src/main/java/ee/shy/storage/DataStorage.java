package ee.shy.storage;

import ee.shy.UnkeyableSimpleMap;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * Map-like abstract storage class for binary data.
 * Access data by hash.
 *
 * @see Hash
 * @see MapStorage
 * @see FileStorage
 */
public abstract class DataStorage implements UnkeyableSimpleMap<Hash, InputStream> {
    /**
     * Adds binary data from input stream to storage.
     * @param source input stream to get data from
     * @return hash of stored data
     */
    @Override
    public final Hash put(InputStream source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(source, md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            IOUtils.copy(dis, baos);

            Hash hash = new Hash(md);
            put(hash, new ByteArrayInputStream(baos.toByteArray()));
            return hash;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds binary data from input stream with precomputed hash.
     * @param hash hash of data from input stream
     * @param source input stream to get data from
     * @throws IOException if there was a problem reading the input stream or writing to some output
     */
    protected abstract void put(Hash hash, InputStream source) throws IOException;

    /**
     * Gets binary data as input stream by hash.
     * Input data is checked against given hash to ensure correctness.
     * @param hash hash of data to get
     * @return input stream of data
     */
    @Override
    public final InputStream get(Hash hash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(getUnchecked(hash), md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            IOUtils.copy(dis, baos);

            Hash hashComputed = new Hash(md);
            if (!hash.equals(hashComputed))
                throw new RuntimeException("stored file content does not match hash");

            return new ByteArrayInputStream(baos.toByteArray());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets binary data as input stream by hash.
     * Input data is <b>not</b> checked against given hash to ensure correctness.
     * @param hash hash of data to get
     * @return input stream of data
     * @throws IOException if there was a problem reading from some input
     */
    public abstract InputStream getUnchecked(Hash hash) throws IOException;
}
