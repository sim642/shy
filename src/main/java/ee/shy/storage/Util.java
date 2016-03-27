package ee.shy.storage;

import java.io.*;

/**
 * Utilities class for storage-related functions.
 */
public class Util {
    private Util() {

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
