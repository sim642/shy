package shy;

import shy.storage.DataStorage;
import shy.storage.MapStorage;
import shy.storage.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StorageTest {
    public static void main(String[] args) throws IOException {
        DataStorage storage = new MapStorage();

        String h1 = storage.add(new ByteArrayInputStream("foo".getBytes(StandardCharsets.UTF_8)));
        String h2 = storage.add(new ByteArrayInputStream("bar".getBytes(StandardCharsets.UTF_8)));

        System.out.println(h1);
        System.out.println(h2);

        InputStream i1 = storage.get(h1);
        System.out.println(new String(Util.toByteArray(i1), "UTF-8"));
        InputStream i2 = storage.get(h2);
        System.out.println(new String(Util.toByteArray(i2), "UTF-8"));
    }
}
