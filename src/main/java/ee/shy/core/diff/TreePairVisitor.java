package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.ObjectUtils.Null;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A visitor of items in a pair of {@link Tree}s.
 * Implementations of this abstract class can be used with {@link TreePairVisitor#walk(Tree, Tree)} to visit all pairs of items in the trees.
 */
public abstract class TreePairVisitor {
    /**
     * Storage to use for getting items.
     */
    private final DataStorage storage;

    /**
     * Constructs a new tree pair visitor with given storage.
     * @param storage storage to use for getting items
     */
    protected TreePairVisitor(DataStorage storage) {
        this.storage = storage;
    }

    /**
     * Invoked before an item is visited.
     * @param prefixPath path in which the item is contained
     * @param name name of the item
     * @throws IOException if an I/O error occurs
     */
    public void preVisitItem(String prefixPath, String name) throws IOException {

    }

    /**
     * Invoked after an item is visited.
     * @param prefixPath path in which the item is contained
     * @param name name of the item
     * @throws IOException if an I/O error occurs
     */
    public void postVisitItem(String prefixPath, String name) throws IOException {

    }

    /**
     * Invoked before a tree is visited.
     * @param prefixPath path in which the tree is contained
     * @param name name of the tree
     * @throws IOException if an I/O error occurs
     */
    public void preVisitTree(String prefixPath, String name) throws IOException {

    }

    /**
     * Invoked after a tree is visited.
     * @param prefixPath path in which the tree is contained
     * @param name name of the tree
     * @throws IOException if an I/O error occurs
     */
    public void postVisitTree(String prefixPath, String name) throws IOException {

    }

    /**
     * Invoked for each pair of file-file in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs input stream of left file
     * @param rhs input stream of right file
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, InputStream lhs, InputStream rhs) throws IOException {

    }

    /**
     * Invoked for each pair of null-file in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs placeholder argument
     * @param rhs input stream of right file
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, Null lhs, InputStream rhs) throws IOException {
        visitPair(prefixPath, name, ClosedInputStream.CLOSED_INPUT_STREAM, rhs);
    }

    /**
     * Invoked for each pair of file-null in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs input stream of left file
     * @param rhs placeholder argument
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, InputStream lhs, Null rhs) throws IOException {
        visitPair(prefixPath, name, lhs, ClosedInputStream.CLOSED_INPUT_STREAM);
    }

    /**
     * Invoked for each pair of tree-tree in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs left tree
     * @param rhs right tree
     * @throws IOException if an I/O error occurs
     */
    public final void visitPair(String prefixPath, String name, Tree lhs, Tree rhs) throws IOException {
        Map<String, TreeItem> lhsItems = lhs.getItems();
        Map<String, TreeItem> rhsItems = rhs.getItems();

        String newPrefixPath = prefixPath != null ? prefixPath : "";
        newPrefixPath += name + "/";

        Set<String> subNameSet = new TreeSet<>();
        subNameSet.addAll(lhsItems.keySet());
        subNameSet.addAll(rhsItems.keySet());

        preVisitTree(prefixPath, name);
        for (String subName : subNameSet) {
            TreeItem lhsItem = lhsItems.get(subName);
            TreeItem rhsItem = rhsItems.get(subName);

            try {
                preVisitItem(newPrefixPath, subName);
                getClass()
                        .getMethod("visitPair", String.class, String.class, getArgClass(lhsItem), getArgClass(rhsItem))
                        .invoke(this, newPrefixPath, subName, getArgValue(lhsItem), getArgValue(rhsItem));
            }
            catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException)
                    throw (IOException) cause;
                else
                    throw new RuntimeException(e);
            }
            catch (NoSuchMethodException e) {
                throw new AssertionError("visitPair method for " + lhsItem.getType() + "-" + rhsItem.getType() + " not found");
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("illegal access to visitPair", e);
            }
            finally {
                postVisitItem(newPrefixPath, subName);
            }
        }
        postVisitTree(prefixPath, name);
    }

    /**
     * Invoked for each pair of null-tree in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs placeholder argument
     * @param rhs right tree
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, Null lhs, Tree rhs) throws IOException {
        visitPair(prefixPath, name, Tree.EMPTY, rhs);
    }

    /**
     * Invoked for each pair of tree-null in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs left tree
     * @param rhs placeholder argument
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, Tree lhs, Null rhs) throws IOException {
        visitPair(prefixPath, name, lhs, Tree.EMPTY);
    }

    /**
     * Invoked for each pair of file-tree in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs input stream of left file
     * @param rhs right tree
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, InputStream lhs, Tree rhs) throws IOException {

    }

    /**
     * Invoked for each pair of tree-file in a tree.
     * @param prefixPath path in which the pair is contained
     * @param name name of the pair
     * @param lhs left tree
     * @param rhs input stream of right file
     * @throws IOException if an I/O error occurs
     */
    public void visitPair(String prefixPath, String name, Tree lhs, InputStream rhs) throws IOException {

    }

    /**
     * Walks the pair of trees from their roots using this tree pair visitor.
     * @param lhs left tree
     * @param rhs right tree
     * @throws IOException if an I/O error occurs
     */
    public void walk(Tree lhs, Tree rhs) throws IOException {
        visitPair(null, "", lhs, rhs);
    }

    /**
     * Returns the {@link Class} of given tree item's value.
     * @param item tree item
     * @return class of tree item's value
     */
    private Class<?> getArgClass(TreeItem item) {
        if (item == null)
            return Null.class;
        switch (item.getType()) {
            case FILE:
                return InputStream.class;

            case TREE:
                return Tree.class;

            default:
                return null;
        }
    }

    /**
     * Returns the given tree item's value.
     * @param item tree item
     * @return tree item's value
     * @throws IOException if an I/O error occurs
     */
    private Object getArgValue(TreeItem item) throws IOException {
        if (item == null)
            return ObjectUtils.NULL;
        switch (item.getType()) {
            case FILE:
                return storage.get(item.getHash());

            case TREE:
                return storage.get(item.getHash(), Tree.class);

            default:
                return null;
        }
    }
}
