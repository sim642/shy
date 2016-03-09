package shy.storage;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;

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
     * Checks equality by comparing hash results.
     * @param other object to compare with
     * @return whether hashes are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) // reflexive
            return true;
        else if (!(other instanceof Hash)) // incompatible
            return false;
        else
            return Arrays.equals(bytes, ((Hash)other).bytes);
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
