package ee.shy.storage;

import java.io.*;

/**
 * Utilities class for storage-related functions.
 */
public class Util {
    private Util() {

    }

    /**
     * Copies one stream to another.
     * @param source source stream
     * @param target target stream
     * @throws IOException
     */
    public static void copyStream(InputStream source, OutputStream target) throws IOException {
        copyStream(source, target, 1024);
    }

    /**
     * Copies one stream to another using given size buffer.
     * @param source source stream
     * @param target target stream
     * @param bufferSize buffer size to use
     * @throws IOException
     */
    public static void copyStream(InputStream source, OutputStream target, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = source.read(buffer, 0, buffer.length)) > 0)
            target.write(buffer, 0, len);
    }

    /**
     * Converts stream data into a byte array.
     * @param source source stream
     * @return byte array of stream data
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyStream(source, baos);
        return baos.toByteArray();
    }

    /**
     * Adds given extension to file path.
     * @param file file path to extend
     * @param extension extension to add
     * @return file path with given extension
     */
    public static File addExtension(File file, String extension) {
        return new File(file.getAbsolutePath() + extension);
    }

    /**
     * Ensures file path's validity by creating required missing parent directories.
     * @param file file path which's validity to ensure
     * @return whether file path is now valid
     */
    public static boolean ensurePath(File file) {
        if (file.isDirectory())
            return true;
        else {
            File parent = file.getParentFile();
            if (parent.exists())
                return parent.isDirectory();
            else
                return parent.mkdirs();
        }
    }
}
