package ee.shy.storage;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class StorageTest {
    public static void main(String[] args) throws IOException {
        DataStorage storage = new FileStorage(
            Arrays.asList(
                new GitFileLocator(Paths.get("teststore")),
                new FlatFileLocator(Paths.get("teststore2"))),
            new AggregateFileAccessor(Arrays.asList(
                new GzipFileAccessor(),
                new PlainFileAccessor())));

        Hash h1 = storage.put(new ByteArrayInputStream("foo".getBytes("UTF-8")));
        Hash h2 = storage.put(new ByteArrayInputStream("bar".getBytes("UTF-8")));
        Hash h3 = storage.put(new FileInputStream("README.md"));

        System.out.println(h1);
        System.out.println(h2);
        System.out.println(h3);

        InputStream i1 = storage.get(new Hash("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"));
        System.out.println(new String(IOUtils.toByteArray(i1), "UTF-8"));
        InputStream i2 = storage.get(h2);
        System.out.println(new String(IOUtils.toByteArray(i2), "UTF-8"));
        InputStream i3 = storage.get(h3);
        IOUtils.copy(i3, new FileOutputStream("README2.md"));

        InputStream i = storage.get(new Hash("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"));
        System.out.println(new String(IOUtils.toByteArray(i), "UTF-8"));
    }
}
