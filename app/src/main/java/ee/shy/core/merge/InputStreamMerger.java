package ee.shy.core.merge;

import ee.shy.io.InputStreamFactory;
import ee.shy.io.PathUtils;
import org.apache.commons.io.IOUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class InputStreamMerger implements Merger<InputStream> {
    private static final DiffMatchPatch DMP = new DiffMatchPatch();

    {
        /*DMP.matchThreshold = 0.4f;
        DMP.patchDeleteThreshold = 0.4f;*/
    }

    @Override
    public void merge(Path path, InputStream original, InputStream revised) throws IOException {
        InputStreamFactory revisedFactory = new InputStreamFactory(revised);

        if (original == null) {
            if (Files.isRegularFile(path)) {
                if (!IOUtils.contentEquals(Files.newInputStream(path), revisedFactory.get())) {
                    // conflict: file differs from added file
                    Files.move(path, PathUtils.addExtension(path, ".OLD"));
                    Files.copy(revisedFactory.get(), PathUtils.addExtension(path, ".REV"));
                }
            }
            else {
                Files.copy(revisedFactory.get(), path);
            }
        }
        else if (revised == null) {
            if (Files.isRegularFile(path)) {
                if (IOUtils.contentEquals(Files.newInputStream(path), original)) {
                    Files.delete(path);
                }
                else {
                    // conflict: file differs from deleted file
                    Files.move(path, PathUtils.addExtension(path, ".OLD"));
                }
            }
        }
        else {
            if (Files.isRegularFile(path)) {
                String patched = merge(Files.newInputStream(path), original, revisedFactory.get());
                if (patched != null)
                    IOUtils.write(patched, Files.newOutputStream(path));
                else {
                    // conflict: edits don't apply
                    Files.move(path, PathUtils.addExtension(path, ".OLD"));
                    Files.copy(revisedFactory.get(), PathUtils.addExtension(path, ".REV"));
                }
            }
            else {
                // conflict: edits in deleted file
                Files.copy(revisedFactory.get(), PathUtils.addExtension(path, ".REV"));
            }
        }
    }

    private String merge(InputStream patchable, InputStream original, InputStream revised) throws IOException {
        String patchableStr = IOUtils.toString(patchable);
        String originalStr = IOUtils.toString(original);
        String revisedStr = IOUtils.toString(revised);

        LinkedList<DiffMatchPatch.Diff> diffs = DMP.diffMain(originalStr, revisedStr);
        DMP.diffCleanupSemantic(diffs);
        DMP.diffCleanupEfficiency(diffs);
        LinkedList<DiffMatchPatch.Patch> patches = DMP.patchMake(originalStr, diffs);

        Object[] patchArray = DMP.patchApply(patches, patchableStr);
        String patchedStr = (String) patchArray[0];
        boolean[] results = (boolean[]) patchArray[1];

        if (results.length == 0 || results[0]) {
            return patchedStr;
        }
        else {
            return null;
        }
    }
}
