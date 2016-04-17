package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;
import ee.shy.core.TreeFileVisitorAdapter;
import ee.shy.core.TreeVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * A simple command for testing.
 */
public class TestCommand implements Command {
    private final String name;

    public TestCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(String[] args) throws IOException {
        System.out.println(name);
        Repository repository = Repository.newExisting();
        Files.walkFileTree(repository.getRootPath(), new TreeFileVisitorAdapter(repository.getRootPath(), new TreeVisitor() {
            @Override
            public void visitFile(String prefixPath, String name, InputStream is) throws IOException {
                System.out.println(prefixPath + " -- " + name);
            }
        }));
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
