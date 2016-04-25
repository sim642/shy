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

    public void visitPair(String prefixPath, String name, InputStream lhs, InputStream rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, Null lhs, InputStream rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, InputStream lhs, Null rhs) throws IOException {

    }

    public void visitPair(String prefixPath, String name, Tree lhs, Tree rhs) throws IOException {
        Map<String, TreeItem> lhsItems = lhs.getItems();
        Map<String, TreeItem> rhsItems = rhs.getItems();

        String newPrefixPath = prefixPath != null ? prefixPath : "";
        newPrefixPath += name + "/";

        Set<String> subNameSet = new TreeSet<>();
        subNameSet.addAll(lhsItems.keySet());
        subNameSet.addAll(rhsItems.keySet());

        for (String subName : subNameSet) {
            Object lhsArg = getArg(lhsItems.get(subName));
            Object rhsArg = getArg(rhsItems.get(subName));

            try {
                getClass()
                        .getMethod("visitPair", String.class, String.class, lhsArg.getClass(), rhsArg.getClass())
                        .invoke(this, newPrefixPath, subName, lhsArg, rhsArg);
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
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

    private Object getArg(TreeItem item) throws IOException {
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
