package ee.shy.storage;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Class to efficiently store the result of a hash function.
 *
 * @see DataStorage
 */
public class Hash {
    /**
     * Hash size in bytes.
     */
    private static final int size = 20; // SHA-1

    /**
     * Hash with zero value to mark non-existence.
     */
    public static final Hash ZERO = new Hash(StringUtils.repeat("00", size));

    /**
     * Array of bytes representing the result of a hash function.
     */
    private final byte[] bytes;

    /**
     * Constructs a hash object from bytes.
     * @param bytes bytes
     * @throws IllegalArgumentException if size is invalid
     */
    public Hash(byte[] bytes) throws IllegalArgumentException {
        this.bytes = bytes;
        checkSize();
    }

    /**
     * Constructs a hash object from hex-string.
     * @param str hex-string
     * @throws IllegalArgumentException if size is invalid
     */
    public Hash(String str) throws IllegalArgumentException {
        bytes = DatatypeConverter.parseHexBinary(str);
        checkSize();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) // reflexive
            return true;
        else if (!(other instanceof Hash)) // incompatible
            return false;
        else
            return Arrays.equals(bytes, ((Hash)other).bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Creates a hex-string of the hash object.
     * @return hex-string
     */
    @Override
    public String toString() {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }

    /**
     * Checks if the hash object has correct size in bytes.
     * @throws IllegalArgumentException if size is invalid
     */
    private void checkSize() throws IllegalArgumentException {
        if (bytes.length != size)
            throw new IllegalArgumentException("Invalid hash size (" + bytes.length + ")");
    }
}
