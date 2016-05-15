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
     * @param path path of the tree
     * @throws IOException if an I/O error occurs
     */
    default void preVisitTree(TreePath path, Tree tree) throws IOException {

    }

    /**
     * Invoked for each file in a tree.
     * @param path path of the file
     * @param is input stream of a file
     * @throws IOException if an I/O error occurs
     */
    default void visitFile(TreePath path, InputStream is) throws IOException {

    }

    /**
     * Invoked after a tree's items are visited.
     * @param path path of the tree
     * @throws IOException if an I/O error occurs
     */
    default void postVisitTree(TreePath path, Tree tree) throws IOException {

    }
}
