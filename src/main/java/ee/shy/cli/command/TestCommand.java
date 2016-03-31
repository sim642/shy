package ee.shy.cli.command;

import ee.shy.cli.ArgumentableCommand;
import ee.shy.storage.Hash;

/**
 * A simple command for testing.
 */
public class TestCommand extends ArgumentableCommand {
    private final String name;

    public TestCommand(String name) {
        this.name = name;
    }

    public boolean execute(String first, String second) {
        System.out.println(first + " - " + second);
        return true;
    }

    public boolean execute(int first) {
        System.out.println(first * 2);
        return true;
    }

    public boolean execute(Hash h) {
        System.out.println("Hash: " + h);
        return true;
    }

    @Override
    public String getHelp() {
        return "Here be TestCommand's help text.";
    }

    @Override
    public String getHelpBrief() {
        return "Command for testing";
    }
}
