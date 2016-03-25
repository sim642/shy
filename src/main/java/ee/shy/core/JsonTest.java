package ee.shy.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        Commit commit;
        try (Reader reader = new InputStreamReader(new FileInputStream("commitTest.json"), "UTF-8")) {
            commit = gson.fromJson(reader, Commit.class);
            reader.close();
        }


        try (Writer writer = new OutputStreamWriter(new FileOutputStream("oJsonTest.json"), "UTF-8")) {
            gson.toJson(commit, writer);
            writer.close();
        }

    }
}
