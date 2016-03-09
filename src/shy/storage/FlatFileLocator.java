package shy.storage;

import java.io.File;

public class FlatFileLocator extends FileLocator {
    public FlatFileLocator(File root) {
        super(root);
    }

    @Override
    public File locate(Hash hash) {
        return new File(root, hash.toString());
    }
}
