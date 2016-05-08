package ee.shy.core.merge;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Merge {
    public static Patch<String> generatePatch(InputStream original, InputStream revised) throws IOException, PatchFailedException {
        return DiffUtils.diff(IOUtils.readLines(original), IOUtils.readLines(revised));
    }

    public static List<String> applyPatch (InputStream patchable, Patch<String> patch) throws IOException, PatchFailedException {
        return patch.applyTo(IOUtils.readLines(patchable));
    }
}
