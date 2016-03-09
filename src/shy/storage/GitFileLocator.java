package shy.storage;

import java.io.File;

public class GitFileLocator extends FileLocator {
    protected static final int DIRECTORY_LENGTH = 2;

    public GitFileLocator(File root) {
        super(root);
    }

    @Override
    public File locate(Hash hash) {
        String directory = hash.toString().substring(0, DIRECTORY_LENGTH);
        String filename = hash.toString().substring(DIRECTORY_LENGTH);
        return new File(new File(root, directory), filename);
    }

    @Override
    public File locateAdd(Hash hash) {
        File file = locate(hash);
        return Util.ensurePath(file) ? file : null;
    }
}
