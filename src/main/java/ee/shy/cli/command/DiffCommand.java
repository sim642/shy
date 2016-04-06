package ee.shy.cli.command;

import difflib.Chunk;
import ee.shy.cli.Command;
import ee.shy.core.diff.FileComparator;

import java.io.File;
import java.io.IOException;

public class DiffCommand implements Command {

    @Override
    public void execute(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Not enough parameters for diff command. See shy help diff.");
        } else {
            FileComparator fileComparator = new FileComparator(new File(args[0]), new File(args[1]));
            System.out.println("\nInserts");
            for (Chunk chunk : fileComparator.getInsertsFromOriginal()) {
                for (int i = 0; i < chunk.size(); i++) {
                    System.out.println(chunk.getLines().get(i).toString());
                }
            }
            System.out.println("\nDelete:");
            for (Chunk chunk : fileComparator.getDeletesFromOriginal()) {
                for (int i = 0; i < chunk.size(); i++) {
                    System.out.println(chunk.getLines().get(i).toString());
                }
            }
            System.out.println("\nChanges:");
            for (Chunk chunk : fileComparator.getChangesFromOriginal()) {
                for (int i = 0; i < chunk.size(); i++) {
                    System.out.println(chunk.getLines().get(i).toString());
                }
            }
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
