package shy;

import shy.storage.DataStorage;
import shy.storage.MapStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

        InputStream i1 = storage.get(h2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = i1.read(buffer, 0, buffer.length)) > 0)
            baos.write(buffer, 0, len);

        System.out.println(baos.toString("UTF-8"));
    }
}
