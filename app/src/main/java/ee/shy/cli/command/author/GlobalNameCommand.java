package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.core.GlobalAuthor;

import java.io.IOException;

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
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
