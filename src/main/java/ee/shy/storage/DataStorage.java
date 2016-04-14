package ee.shy.storage;

import ee.shy.io.Json;
import ee.shy.io.Jsonable;
import ee.shy.map.UnkeyableSimpleMap;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
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
public abstract class DataStorage implements UnkeyableSimpleMap<Hash, InputStream> {
    /**
     * Adds binary data from input stream to storage.
     * @param source input stream to get data from
     * @return hash of stored data
     */
    @Override
    public final Hash put(InputStream source) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            try (DigestInputStream dis = new DigestInputStream(source, md);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                IOUtils.copy(dis, baos);

                Hash hash = new Hash(md.digest());
                put(hash, new ByteArrayInputStream(baos.toByteArray()));
                return hash;
            }
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds object as JSON to storage.
     * @param object object to store
     * @return hash of stored data
     */
    public final Hash put(Jsonable object) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 DigestOutputStream dos = new DigestOutputStream(baos, md)) {

                object.write(dos);

                Hash hash = new Hash(md.digest());
                put(hash, new ByteArrayInputStream(baos.toByteArray()));
                return hash;
            }
        }
        catch (NoSuchAlgorithmException e) {
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
    public final InputStream get(Hash hash) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            try (InputStream is = getUnchecked(hash)) {
                if (is == null)
                    return null;

                try (DigestInputStream dis = new DigestInputStream(is, md);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                    IOUtils.copy(dis, baos);

                    Hash hashComputed = new Hash(md.digest());
                    if (!hash.equals(hashComputed))
                        throw new DataIntegrityException(hash, hashComputed);

                    return new ByteArrayInputStream(baos.toByteArray());
                }
            }
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets object from JSON by hash.
     * @param hash hash of object to get
     * @param classofT class of object to get
     * @param <T> type of obect to get
     * @return object from JSON
     * @throws IOException if JSON deserialization from underlying stream fails
     */
    public final <T> T get(Hash hash, Class<T> classofT) throws IOException {
        return Json.read(get(hash), classofT);
    }

    /**
     * Gets binary data as input stream by hash.
     * Input data is <b>not</b> checked against given hash to ensure correctness.
     * @param hash hash of data to get
     * @return input stream of data
     * @throws IOException if there was a problem reading from some input
     */
    protected abstract InputStream getUnchecked(Hash hash) throws IOException;
}
