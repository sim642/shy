package ee.shy.cli;

import ee.shy.Builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for building help text with preset format
 */
public class HelptextBuilder implements Builder<String> {

    /**
     * Data structure that contains command's argument and its corresponding description.
     */
    private final Map<String, String> commandWithArgs = new LinkedHashMap<>();

    /**
     * List that provides information about executing the command without arguments.
     */
    private final List<String> commandWithoutArgs = new ArrayList<>();

    /**
     * List containing additional information about the command.
     */
    private final List<String> descriptions = new ArrayList<>();

    public HelptextBuilder addWithArgs(String command, String description) {
        commandWithArgs.put(command, description);
        return this;
    }

    public HelptextBuilder addWithoutArgs(String description) {
        commandWithoutArgs.add(description);
        return this;
    }

    public HelptextBuilder addDescription(String description) {
        descriptions.add(description);
        return this;
    }

    /**
     * Create a StringBuilder object to create a formatted help text.
     *
     * @return formatted help text
     */
    @Override
    public String create() {
        StringBuilder helptext = new StringBuilder();
        if (!commandWithArgs.isEmpty()) {
            helptext.append("Usage with arguments:\n");
            for (Map.Entry<String, String> entry : commandWithArgs.entrySet()) {
                helptext.append("\t").append(entry.getKey()).append("\n");
                helptext.append("\t\t- ").append(entry.getValue()).append("\n");
            }
            helptext.append("\n");
        }
        if (!commandWithoutArgs.isEmpty()) {
            helptext.append("Usage without arguments:\n");
            for (String commandWithoutArg : commandWithoutArgs) {
                helptext.append("\t").append(commandWithoutArg).append("\n");
            }
            helptext.append("\n");
        }
        if (!descriptions.isEmpty()) {
            helptext.append("Description:\n");
            for (String description : descriptions) {
                helptext.append("\t").append(description).append("\n");
            }
            helptext.append("\n");
        }
        return helptext.toString();
    }
}
