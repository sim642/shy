package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.GlobalAuthor;

import java.io.IOException;

/**
 * Command to alter with config's email value.
 */
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
        return new HelptextBuilder()
                .addWithArgs("<email>", "Set author's email at the global configuration file")
                .addWithoutArgs("Get author's email from the global configuration file")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Set and get the email value of the global configuration file";
    }
}
