package ee.shy.core.diff;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * This is necessary because Util class must not be initialized.
     */
    private Util () {}

    /**
     * Take lines as parameter and output them with colors.
     * Green color indicating additions.
     * Red color indicating removed lines.
     * @param diffLines
     */
    public static void outputDiff(List<String> diffLines) {
        AnsiConsole.systemInstall();
        for (String row : diffLines) {
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
     * Gets the file's contents and put it in a List.
     * @param filePath path to file
     * @return List containing all the lines from given file
     * @throws IOException
     */
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
