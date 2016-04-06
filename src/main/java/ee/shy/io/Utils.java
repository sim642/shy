package ee.shy.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    private Utils() {

    }

    public static Path getCurrentPath() {
        return Paths.get(System.getProperty("user.dir"));
    }
}
