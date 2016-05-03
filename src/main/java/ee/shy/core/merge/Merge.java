package ee.shy.core.merge;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Merge {
    public static Patch generatePatch(InputStream original, InputStream revised) throws IOException, PatchFailedException {
        return DiffUtils.diff(IOUtils.readLines(original), IOUtils.readLines(revised));
    }

    public static List<String> applyPatch (InputStream patchable, Patch patch) throws IOException, PatchFailedException {
        List<?> patchableStrings = IOUtils.readLines(patchable);
        patchableStrings = patch.applyTo(patchableStrings);

        // FIXME: 3.05.16 Make own fork to support generic types or migrate to https://github.com/KengoTODA/java-diff-utils/
        return (List<String>) patchableStrings;
    }
}
