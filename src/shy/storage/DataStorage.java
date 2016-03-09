package shy.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class DataStorage {
    public final String add(InputStream source) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream dis = new DigestInputStream(source, md);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Util.copyStream(dis, baos);

            String hash = Util.toString(md);
            add(hash, new ByteArrayInputStream(baos.toByteArray()));
            return hash;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void add(String hash, InputStream source) throws IOException;
    public abstract InputStream get(String hash) throws IOException;
}
