package ee.shy.cli.command;

import com.jcraft.jsch.JSchException;
import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.SshRepository;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A simple command for testing.
 */
public class TestCommand implements Command {
    public TestCommand() {
    }

    @Override
    public void execute(String[] args) throws IOException {
        try (SshRepository repository = SshRepository.newRemote()) {
            System.out.println(repository.getBranches().keySet());
        }
        catch (JSchException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("test1", "test1Description")
            .addWithArgs("test2", "test2Description")
            .addWithArgs("test3", "test3Description")

            .addWithoutArgs("test4WithoutArgs")
            .addWithoutArgs("test5WithoutArgs")
            .addWithoutArgs("test6WithoutArgs")

            .addDescription("test7Description")
            .addDescription("test8Description")
            .addDescription("test9Description")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Command for testing";
    }
}
