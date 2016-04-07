package ee.shy.core.diff;

import difflib.DiffUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileComparator {

    private final Path original;
    private final Path comparable;

    private static final int CONTEXT_SIZE = 5;

    public FileComparator(Path original, Path comparable) {
        this.comparable = comparable;
        this.original = original;
    }

    public List<String> getDiffRows() throws IOException {
        final List<String> originalFileLines = Util.fileToLines(original);
        final List<String> revisedFileLines = Util.fileToLines(comparable);
        return DiffUtils.generateUnifiedDiff(original.toString(), comparable.toString(), originalFileLines,
                DiffUtils.diff(originalFileLines, revisedFileLines), CONTEXT_SIZE);
    }


}
