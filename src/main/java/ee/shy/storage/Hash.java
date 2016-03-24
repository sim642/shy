package ee.shy.storage;

import org.apache.commons.lang3.StringUtils;

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
     * Hash with zero value to mark non-existence.
     */
    public static final Hash ZERO = new Hash(StringUtils.repeat("00", 20));

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
}
