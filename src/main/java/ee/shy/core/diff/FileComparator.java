package ee.shy.core.diff;

import difflib.DiffUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


// TODO: 8.04.16 Make methods static. Remove constructor
public class FileComparator  {

    private final Path original;
    private final Path comparable;

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
        // TODO: 10.04.16 Test this on Windows OS.
        AnsiConsole.systemInstall();
        for (String row : getDiffRows()) {
            switch (row.charAt(0)) {
                case '+':
                    System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a(row).reset());
                    break;
                case '-':
                    System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a(row).reset());
                    break;
                default:
                    System.out.println(row);
                    break;
            }
        }
        AnsiConsole.systemUninstall();
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
