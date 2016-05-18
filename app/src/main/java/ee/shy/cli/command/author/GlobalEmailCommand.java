package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.core.GlobalAuthor;

import java.io.IOException;

public class GlobalEmailCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        GlobalAuthor globalAuthor = new GlobalAuthor();
        if (args.length == 0) {
            String email = globalAuthor.getGlobalEmail();
            if (email == null) {
                System.out.println("Email not set in .shyconfig file. See 'shy help gauthor email'");
            } else {
                System.out.println(email);
            }
        } else {
            globalAuthor.setGlobalEmail(args[0]);
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
