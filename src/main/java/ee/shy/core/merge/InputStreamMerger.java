package ee.shy.core.merge;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InputStreamMerger implements Merger<InputStream> {
    private Patch<String> generatePatch(InputStream original, InputStream revised) throws IOException, PatchFailedException {
        return DiffUtils.diff(IOUtils.readLines(original), IOUtils.readLines(revised));
}

    private List<String> applyPatch(InputStream patchable, Patch<String> patch) throws IOException, PatchFailedException {
        return patch.applyTo(IOUtils.readLines(patchable));
    }

    @Override
    public InputStream merge(InputStream original, InputStream revised, InputStream patchable) throws IOException, PatchFailedException {
        StringBuilder sb = new StringBuilder();
        List<String> mergedStrings = applyPatch(patchable, generatePatch(original, revised));
        for (String mergedString : mergedStrings) {
            sb.append(mergedString).append("\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
