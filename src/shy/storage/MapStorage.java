package shy.storage;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MapStorage implements DataStorage {
    private final Map<String, byte[]> storage;

    public MapStorage() {
        storage = new HashMap<>();
    }

    @Override
    public String add(InputStream source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(source, md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = dis.read(buffer, 0, buffer.length)) > 0)
                baos.write(buffer, 0, len);

            String hash = DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
            storage.put(hash, baos.toByteArray());
            return hash;
        }
        catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream get(String hash) {
        byte[] buffer = storage.get(hash);
        if (buffer != null)
            return new ByteArrayInputStream(buffer);
        else
            return null;
    }
}
