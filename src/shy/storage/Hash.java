package shy.storage;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * Class to efficiently store the result of a hash function.
 *
 * @see DataStorage
 */
public class Hash {
    /**
     * Array of bytes representing the result of a hash function.
     */
    private final byte[] bytes;

    /**
     * Constructs a hash object from digested message.
     * @param md message digester
     */
    public Hash(MessageDigest md) {
        bytes = md.digest();
    }

    /**
     * Constructs a hash object from hex-string.
     * @param str hex-string
     */
    public Hash(String str) {
        bytes = DatatypeConverter.parseHexBinary(str);
    }

    /**
     * Creates a hex-string of the hash object.
     * @return hex-string
     */
    @Override
    public String toString() {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }
}
