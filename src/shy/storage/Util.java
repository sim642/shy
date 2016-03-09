package shy.storage;

import java.io.*;

public class Util {
    private Util() {

    }

    public static void copyStream(InputStream source, OutputStream target) throws IOException {
        copyStream(source, target, 1024);
    }

    public static void copyStream(InputStream source, OutputStream target, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = source.read(buffer, 0, buffer.length)) > 0)
            target.write(buffer, 0, len);
    }

    public static byte[] toByteArray(InputStream source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyStream(source, baos);
        return baos.toByteArray();
    }

    public static File addExtension(File file, String extension) {
        return new File(file.getAbsolutePath() + extension);
    }
}
