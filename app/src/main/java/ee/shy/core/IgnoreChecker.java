package ee.shy.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class IgnoreChecker {

    private final List<PathMatcher> pathMatchers = new ArrayList<>();

    private final Path rootPath;

    public IgnoreChecker(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        Path shyIgnorePath = rootPath.resolve(".shyignore");
        if (Files.exists(shyIgnorePath)) {
            pathMatchers.addAll(Files.readAllLines(shyIgnorePath).stream().map(
                    string -> FileSystems.getDefault().getPathMatcher("glob:" + string)).collect(Collectors.toList())
            );
        }
    }

    public boolean isIgnored(Path path) throws IOException {
        if (Files.isHidden(path)) return true;
        for (PathMatcher pathMatcher : pathMatchers) {
            if (pathMatcher.matches(rootPath.relativize(path.normalize().toAbsolutePath()))) {
                return true;
            }
        }
        return false;
    }
}
