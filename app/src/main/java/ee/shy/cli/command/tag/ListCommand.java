package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Tag;
import ee.shy.map.NamedObjectMap;

import java.io.IOException;

/**
 * Command to list all existing tags.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        NamedObjectMap<Tag> tags = LocalRepository.newExisting().getTags();
        for (String tagName : tags.keySet()) {
            System.out.println(tagName + " - " + tags.get(tagName).getMessage());
        }
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
