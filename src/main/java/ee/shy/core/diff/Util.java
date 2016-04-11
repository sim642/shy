package ee.shy.core.diff;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

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
}
