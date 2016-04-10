package ee.shy.core.diff;

import difflib.DiffUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileDiffer implements Differ<Path> {

    /**
     * Constant defining how many rows before and after each difference is shown.
     */
    private static final int CONTEXT_SIZE = 5;

    /**
     * Uses DiffUtils to generate an unified diff.
     * @return List of diff strings
     * @throws IOException
     */
    @Override
    public List<String> diff(Path original, Path revised) throws IOException {
        final List<String> originalFileLines = Util.fileToLines(original);
        final List<String> revisedFileLines = Util.fileToLines(revised);
        return DiffUtils.generateUnifiedDiff(original.toString(), revised.toString(), originalFileLines,
                DiffUtils.diff(originalFileLines, revisedFileLines), CONTEXT_SIZE);
    }
}
