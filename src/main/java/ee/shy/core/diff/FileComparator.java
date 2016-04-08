package ee.shy.core.diff;

import difflib.DiffUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileComparator {

    private final Path original;
    private final Path comparable;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constant defining how many rows before and after each difference is shown.
     */
    private static final int CONTEXT_SIZE = 5;

    public FileComparator(Path original, Path comparable) {
        this.comparable = comparable;
        this.original = original;
    }

    /**
     * Outputs {@link #getDiffRows()}'s output to terminal with colors indicating additions and removes.
     * @throws IOException
     */
    public void outputColorizedDiff() throws IOException {
        for (String row :getDiffRows()) {
            switch (row.charAt(0)) {
                case '+':
                    System.out.println(ANSI_GREEN + row + ANSI_RESET);
                    break;
                case '-':
                    System.out.println(ANSI_RED + row + ANSI_RESET);
                    break;
                default:
                    System.out.println(row);
                    break;
            }
        }
    }

    /**
     * Output {@link #getDiffRows()}'s output to terminal without colors.
     * @throws IOException
     */
    public void outputDiff() throws IOException {
        getDiffRows().forEach(System.out::println);
    }

    /**
     * Uses DiffUtils to generate an unified diff
     * @return
     * @throws IOException
     */
    private List<String> getDiffRows() throws IOException {
        final List<String> originalFileLines = Util.fileToLines(original);
        final List<String> revisedFileLines = Util.fileToLines(comparable);
        return DiffUtils.generateUnifiedDiff(original.toString(), comparable.toString(), originalFileLines,
                DiffUtils.diff(originalFileLines, revisedFileLines), CONTEXT_SIZE);
    }
}
