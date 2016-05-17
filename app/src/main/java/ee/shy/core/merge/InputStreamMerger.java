package ee.shy.core.merge;

import ee.shy.io.PathUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputStreamMerger implements Merger<InputStream> {
    @Override
    public void merge(Path path, InputStream original, InputStream revised) throws IOException {
        // TODO: 17.05.16 simple automatic merging
        if (Files.exists(path))
            Files.move(path, PathUtils.addExtension(path, ".OLD"));
        Files.copy(revised, PathUtils.addExtension(path, ".REV"));
    }
}
