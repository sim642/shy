package shy.storage;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class Hash {
    private final byte[] bytes;

    public Hash(MessageDigest md) {
        bytes = md.digest();
    }

    public Hash(String str) {
        bytes = DatatypeConverter.parseHexBinary(str);
    }

    @Override
    public String toString() {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }
}
