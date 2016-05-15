package ee.shy.core.diff;

import difflib.DiffUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Class to get the difference of two given InputStreams.
 */
public class InputStreamDiffer implements Differ<InputStream> {

    /**
     * Constant defining how many rows before and after each difference is shown.
     */
    private static final int CONTEXT_SIZE = 5;

    @Override
    public List<String> diff(InputStream original, InputStream revised) throws IOException {
        List<String> originalLines = IOUtils.readLines(original);
        List<String> revisedLines = IOUtils.readLines(revised);
        List<String> diffLines = DiffUtils.generateUnifiedDiff(null, null,
                originalLines, DiffUtils.diff(originalLines,
                        revisedLines), CONTEXT_SIZE);

        if (diffLines.size() > 2) {
            return diffLines.subList(2, diffLines.size());
        } else
            return Collections.emptyList();
    }

    @Override
    public List<String> shortDiff(InputStream original, InputStream revised) throws IOException {
        return Collections.emptyList();
    }
}
