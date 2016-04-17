package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newExisting().getTags().keySet().forEach(System.out::println);
        // TODO: 18.04.16 Output tags with messages
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
