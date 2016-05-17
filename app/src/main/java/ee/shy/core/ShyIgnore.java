package ee.shy.core;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class ShyIgnore {

    private static final List<String> globs = new ArrayList<>();

    static {
        Path path = Paths.get(".shyignore");
        if (Files.exists(path)) {
            try {
                globs.addAll(IOUtils.readLines(Files.newInputStream(path)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isIgnored(Path path) {
        for (String glob : globs) {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**" + glob);
            if (pathMatcher.matches(path)) return true;
        }
        return false;
    }
}
