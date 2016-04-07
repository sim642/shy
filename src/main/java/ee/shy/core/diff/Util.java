package ee.shy.core.diff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private Util () {}

    public static boolean isOSWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static List<String> fileToLines(Path filePath) throws IOException {
        final List<String> lines = new ArrayList<String>();
        String line;
        final BufferedReader in = new BufferedReader(new FileReader(filePath.toFile()));
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
}
