package ee.shy.core;

import ee.shy.storage.DataStorage;

import java.io.IOException;
import java.io.InputStream;

/**
 * A visitor of items in a {@link Tree}.
 * Implementations of this interface can be used with {@link Tree#walk(DataStorage, TreeVisitor)} to visit each item in the tree.
 */
public interface TreeVisitor {
    /**
     * Invoked before a tree's items are visited.
     * @param prefixPath path in which a tree is contained
     * @param name name of a tree
     * @throws IOException if an I/O error occurs
     */
    default void preVisitTree(String prefixPath, String name, Tree tree) throws IOException {

    }

    /**
     * Invoked for each file in a tree.
     * @param prefixPath path in which a file is contained
     * @param name name of a file
     * @param is input stream of a file
     * @throws IOException if an I/O error occurs
     */
    default void visitFile(String prefixPath, String name, InputStream is) throws IOException {

    }

    /**
     * Invoked after a tree's items are visited.
     * @param prefixPath path in which a tree is contained
     * @param name name of a tree
     * @throws IOException if an I/O error occurs
     */
    default void postVisitTree(String prefixPath, String name, Tree tree) throws IOException {

    }
}
