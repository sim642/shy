package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.GlobalAuthor;

import java.io.IOException;

/**
 * Command to alter with config's name value.
 */
public class GlobalNameCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        GlobalAuthor globalAuthor = new GlobalAuthor();
        if (args.length == 0) {
            String name = globalAuthor.getGlobalName();
            if (name == null) {
                System.out.println("Name not set in .shyconfig file. See 'shy help gauthor name'");
            } else {
                System.out.println(name);
            }
        } else {
            globalAuthor.setGlobalName(args[0]);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<name>", "Set author's name at the global configuration file")
                .addWithoutArgs("Get author's name from the global configuration file")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Set and get the name value of the global configuration file";
    }
}
