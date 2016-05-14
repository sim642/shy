package ee.shy.cli.command.remote;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Remote;
import ee.shy.map.Named;
import ee.shy.map.NamedObjectMap;

import java.io.IOException;

/**
 * Command for listing existing remotes.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        NamedObjectMap<Remote> remotes = LocalRepository.newExisting().getRemotes();
        for (Named<Remote> namedRemote : remotes.entrySet()) {
            System.out.println(namedRemote.getName() + " - " + namedRemote.getValue().getURI());
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithoutArgs("List all remotes")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "List remotes";
    }
}
