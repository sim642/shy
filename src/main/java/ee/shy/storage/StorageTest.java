package ee.shy.storage;

import java.io.*;
import java.util.Arrays;

public class StorageTest {
    public static void main(String[] args) throws IOException {
        DataStorage storage = new FileStorage(
            Arrays.asList(
                new GitFileLocator(new File("teststore")),
                new FlatFileLocator(new File("teststore2"))),
            new AggregateFileAccessor(Arrays.asList(
                new GzipFileAccessor(),
                new PlainFileAccessor())));

        /*Hash h1 = storage.add(new ByteArrayInputStream("foo".getBytes("UTF-8")));
        Hash h2 = storage.add(new ByteArrayInputStream("bar".getBytes("UTF-8")));
        Hash h3 = storage.add(new FileInputStream("README.md"));

        System.out.println(h1);
        System.out.println(h2);
        System.out.println(h3);

        InputStream i1 = storage.get(new Hash("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"));
        System.out.println(new String(Util.toByteArray(i1), "UTF-8"));
        InputStream i2 = storage.get(h2);
        System.out.println(new String(Util.toByteArray(i2), "UTF-8"));
        InputStream i3 = storage.get(h3);
        Util.copyStream(i3, new FileOutputStream("README2.md"));*/

        InputStream i = storage.get(new Hash("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33"));
        System.out.println(new String(Util.toByteArray(i), "UTF-8"));
    }
}
