package shy;

import shy.storage.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StorageTest {
    public static void main(String[] args) throws IOException {
        DataStorage storage = new FileStorage(new FlatFileLocator(new File("teststore")), new GzipFileAccessor());

        Hash h1 = storage.add(new ByteArrayInputStream("foo".getBytes(StandardCharsets.UTF_8)));
        Hash h2 = storage.add(new ByteArrayInputStream("bar".getBytes(StandardCharsets.UTF_8)));

        System.out.println(h1);
        System.out.println(h2);

        InputStream i1 = storage.get(new Hash("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"));
        System.out.println(new String(Util.toByteArray(i1), "UTF-8"));
        InputStream i2 = storage.get(h2);
        System.out.println(new String(Util.toByteArray(i2), "UTF-8"));
    }
}
