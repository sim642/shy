package ee.shy.cli.command.remote;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Remote;
import ee.shy.map.Named;

import java.io.IOException;

/**
 * Command for listing existing remotes.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        for(Named<Remote> namedRemote : LocalRepository.newExisting().getRemotes().entrySet()) {
            System.out.println(namedRemote.getName() + " - " + namedRemote.getValue().getURI().toString());
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
