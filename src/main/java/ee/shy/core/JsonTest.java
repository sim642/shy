package ee.shy.core;

import java.io.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        Json json = new Json();
        FileInputStream is = new FileInputStream("treeTest.json");
        Tree tree = json.read(is, Tree.class);
        json.write(new FileOutputStream(new File("oTreeTest.json")), tree);
    }
}
