package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.SuperCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompletionCommand implements Command {
    private final Command rootCommand;

    public CompletionCommand(Command rootCommand) {
        this.rootCommand = rootCommand;
    }

    @Override
    public void execute(String[] args) throws IOException {
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        argsList.add(null);

        Command command = rootCommand;
        for (String arg : argsList) {
            if (command instanceof SuperCommand) {
                Map<String, Command> subCommands = ((SuperCommand) command).getSubCommands();
                Command subCommand = subCommands.get(arg);
                if (subCommand != null) {
                    command = subCommand;
                }
                else {
                    System.out.println(String.join(" ", subCommands.keySet()));
                    break;
                }
            }
            else if (command instanceof HelpCommand) {
                command = ((HelpCommand) command).getRootCommand();
            }
        }
    }

    @Override
    public String getHelp() {
        return null;
    }
}
