package shy.storage;

import java.io.File;

public abstract class FileLocator {
    protected final File root;

    public FileLocator(File root) {
        if (!root.exists())
            root.mkdirs();
        if (!root.isDirectory())
            throw new RuntimeException("root must be a directory"); // TODO: 9.03.16 throw better exception

        this.root = root;
    }

    public abstract File get(Hash hash);
}
