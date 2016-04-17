package ee.shy.core;

import java.io.IOException;
import java.io.InputStream;

public interface TreeVisitor {
    void visitFile(String prefixPath, String name, InputStream is) throws IOException;

    void preVisitTree(String prefixPath, String name) throws IOException;
    void postVisitTree(String prefixPath, String name) throws IOException;
}
