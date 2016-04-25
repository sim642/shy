package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.ObjectUtils.Null;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public abstract class TreePairVisitor {
    protected final DataStorage storage;

    protected TreePairVisitor(DataStorage storage) {
        this.storage = storage;
    }

    public void preVisitItem(String prefixPath, String name) throws IOException {

    }

    public void postVisitItem(String prefixPath, String name) throws IOException {

    }

    public void preVisitTree(String prefixPath, String name) throws IOException {

    }

    public void postVisitTree(String prefixPath, String name) throws IOException {

    }

    public void visitPair(String prefixPath, String name, InputStream lhs, InputStream rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, Null lhs, InputStream rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, InputStream lhs, Null rhs) throws IOException {

    }

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
                postVisitItem(newPrefixPath, subName);
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
        }
        postVisitTree(prefixPath, name);
    }

    public void visitPair(String prefixPath, String name, Null lhs, Tree rhs) throws IOException {
        visitPair(prefixPath, name, Tree.EMPTY, rhs);
    }

    public void visitPair(String prefixPath, String name, Tree lhs, Null rhs) throws IOException {
        visitPair(prefixPath, name, lhs, Tree.EMPTY);
    }

    public void visitPair(String prefixPath, String name, InputStream lhs, Tree rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, Tree lhs, InputStream rhs) throws IOException {

    }

    public void walk(Tree lhs, Tree rhs) throws IOException {
        visitPair(null, "", lhs, rhs);
    }

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
