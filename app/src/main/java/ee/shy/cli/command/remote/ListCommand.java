package ee.shy.cli.command.remote;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;

import java.io.IOException;

/**
 * Command for listing existing remotes.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        for(String key : LocalRepository.newExisting().getRemotes().keySet()) {
            System.out.println(key + " - " + LocalRepository.newExisting().getRemotes().get(key).getURI().toString());
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithoutArgs("List all existing remote URIs")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "List remote URIs";
    }
}
