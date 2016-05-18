package ee.shy.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class which handles file ignoring
 */
public final class IgnoreChecker {

    /**
     * Contains all the matchers read from .shyignore
     */
    private final List<PathMatcher> pathMatchers;

    /**
     * Repository's root path
     */
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

    /**
     * Determines whether a path is ignored or not.
     * Path is ignored if it is hidden or matches any {@link PathMatcher} from the {@link IgnoreChecker#pathMatchers} list.
     * @param path path to evaluate
     * @return true if path is ignored, false otherwise
     * @throws IOException
     */
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
