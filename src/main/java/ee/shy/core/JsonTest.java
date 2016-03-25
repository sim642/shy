package ee.shy.core;

import java.io.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        Json json = new Json();
        json.readCommitJson();
        json.writeCommitJson();
        //Json.gson.toJson(json.)
    }
}
