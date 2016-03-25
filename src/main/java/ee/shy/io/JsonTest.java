package ee.shy.io;

import ee.shy.core.Commit;
import ee.shy.core.Tree;

import java.io.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        {
            FileInputStream is = new FileInputStream("commitTest.json");
            Commit commit = Json.read(is, Commit.class);
            commit.write(new FileOutputStream(new File("oJsonTest.json")));
        }

        {
            FileInputStream is = new FileInputStream("treeTest.json");
            Tree tree = Json.read(is, Tree.class);
            tree.write(new FileOutputStream(new File("oTreeTest.json")));
        }
    }
}
