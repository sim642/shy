package ee.shy.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class IgnoreChecker {

    private final List<PathMatcher> pathMatchers;

    private final Path rootPath;

    public IgnoreChecker(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        Path shyIgnorePath = rootPath.resolve(".shyignore");
        if (Files.exists(shyIgnorePath)) {
            pathMatchers = Files.lines(shyIgnorePath).map(
                    string -> FileSystems.getDefault().getPathMatcher("glob:" + string)).collect(Collectors.toList()
            );
        } else {
            pathMatchers = Collections.EMPTY_LIST;
        }

    }

    public boolean isIgnored(Path path) throws IOException {
        if (Files.isHidden(path)) return true;
        Path matchablePath = rootPath.relativize(path.normalize().toAbsolutePath());
        for (PathMatcher pathMatcher : pathMatchers) {
            if (pathMatcher.matches(matchablePath)) {
                return true;
            }
        }
        return false;
    }
}
