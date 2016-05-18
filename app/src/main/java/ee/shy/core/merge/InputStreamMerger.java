package ee.shy.core.merge;

import ee.shy.io.PathUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class InputStreamMerger implements Merger<InputStream> {
    private static final DiffMatchPatch DMP = new DiffMatchPatch();

    @Override
    public void merge(Path path, InputStream original, InputStream revised) throws IOException {
        String originalStr = IOUtils.toString(original);
        String revisedStr = IOUtils.toString(revised);
        LinkedList<DiffMatchPatch.Diff> diffs = DMP.diffMain(originalStr, revisedStr);
        DMP.diffCleanupSemantic(diffs);
        LinkedList<DiffMatchPatch.Patch> patches = DMP.patchMake(originalStr, diffs);

        String patchableStr = IOUtils.toString(Files.exists(path) ? Files.newInputStream(path) : ClosedInputStream.CLOSED_INPUT_STREAM);
        Object[] patchArray = DMP.patchApply(patches, patchableStr);
        String patchedStr = (String) patchArray[0];
        boolean[] results = (boolean[]) patchArray[1];

        if (results.length == 0 || results[0]) {
            IOUtils.write(patchedStr, Files.newOutputStream(path));
        }
        else {
            if (Files.exists(path))
                Files.move(path, PathUtils.addExtension(path, ".OLD"));
            Files.copy(revised, PathUtils.addExtension(path, ".REV"));
        }
    }
}
