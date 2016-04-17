package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command to list all existing tags.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newExisting().getTags().keySet().forEach(System.out::println);
        // TODO: 18.04.16 Output tags with messages
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithoutArgs("Display each existing tag with its corresponding message.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "List existing tags";
    }
}
