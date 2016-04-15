package ee.shy.core.diff;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffUtils {
    /**
     * Map containing character and its corresponding color.
     */
    private static final Map<Character, Ansi.Color> colors = new HashMap<>();

    /**
     * Initialize colors map when class is loaded.
     */
    static {
        colors.put('@', Ansi.Color.CYAN);
        colors.put('+', Ansi.Color.GREEN);
        colors.put('-', Ansi.Color.RED);
    }

    /**
     * This is necessary because DiffUtils class must not be initialized.
     */
    private DiffUtils() {

    }

    /**
     * Take lines as parameter and output them with colors.
     * Green color indicating additions.
     * Red color indicating removed lines.
     * @param diffLines unified diff lines
     */
    public static void outputDiff(List<String> diffLines) {
        AnsiConsole.systemInstall();
        for (String row : diffLines) {
            if (!row.isEmpty() && colors.containsKey(row.charAt(0))) {
                System.out.println(Ansi.ansi().fg(colors.get(row.charAt(0))).a(row).reset());
            } else {
                System.out.println(row);
            }
        }
        AnsiConsole.systemUninstall();
    }
}
