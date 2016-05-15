package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.core.TreePath;
import ee.shy.storage.DataStorage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TreePairs {

    interface Visitor {
        void visitFilePair(TreePath path, TreeItem lhs, TreeItem rhs) throws IOException;
        void visitTreePair(TreePath path, TreeItem lhs, TreeItem rhs) throws IOException;
    }

    private static abstract class Pair implements Comparable<Pair> {
        protected final TreePath path;
        protected final TreeItem lhs;
        protected final TreeItem rhs;

        public Pair(TreePath path, TreeItem lhs, TreeItem rhs) {
            this.path = path;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public abstract void visit(Visitor visitor) throws IOException;

        public abstract Pair flip();

        @Override
        public int compareTo(Pair o) {
            int treePathCompare = path.compareTo(o.path);
            if (treePathCompare == 0) {
                if (o.lhs != null && o.rhs == null) {
                    return 1;
                }
                else if (o.lhs == null && o.rhs != null) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
            else {
                return treePathCompare;
            }
        }
    }

    private static class FilePair extends Pair {
        public FilePair(TreePath path, TreeItem lhs, TreeItem rhs) {
            super(path, lhs, rhs);
        }

        @Override
        public Pair flip() {
            return new FilePair(path, rhs, lhs);
        }

        @Override
        public void visit(Visitor visitor) throws IOException {
            visitor.visitFilePair(path, lhs, rhs);
        }
    }

    private static class TreePair extends Pair {
        public TreePair(TreePath path, TreeItem lhs, TreeItem rhs) {
            super(path, lhs, rhs);
        }

        @Override
        public Pair flip() {
            return new TreePair(path, rhs, lhs);
        }

        @Override
        public void visit(Visitor visitor) throws IOException {
            visitor.visitTreePair(path, lhs, rhs);
        }
    }

    public static void visitPairs(DataStorage storage, Tree lhs, Tree rhs, Visitor visitor) throws IOException {
        List<Pair> lhsPairs = new ArrayList<>();
        walk(storage, lhsPairs, new TreePath(), lhs, rhs);

        List<Pair> rhsPairs = new ArrayList<>();
        walk(storage, rhsPairs, new TreePath(), rhs, lhs);
        rhsPairs = rhsPairs.stream().map(Pair::flip).collect(Collectors.toList());

        // use TreeSet+Comparable for great profit:
        // + remove duplicate pairs of lhs == rhs
        // + order deletes before adds
        // + order pairs by path
        Set<Pair> pairs = new TreeSet<>();
        pairs.addAll(lhsPairs);
        pairs.addAll(rhsPairs);

        for (Pair pair : pairs) {
            pair.visit(visitor);
        }
    }

    private static void walk(DataStorage storage, List<Pair> pairs, TreePath path, Tree lhs, Tree rhs) throws IOException {
        for (Map.Entry<String, TreeItem> entry : lhs.getItems().entrySet()) {
            String itemPath = entry.getKey();
            TreeItem lhsItem = entry.getValue();
            TreeItem rhsItem = rhs.getItems().get(itemPath);
            TreePath fullPath = path.resolve(itemPath);
            if (lhsItem.getType() == TreeItem.Type.FILE) {
                if (rhsItem == null || rhsItem.getType() == TreeItem.Type.TREE) {
                    pairs.add(new FilePair(fullPath, lhsItem, null));
                }
                if (rhsItem != null && rhsItem.getType() == TreeItem.Type.FILE) {
                    pairs.add(new FilePair(fullPath, lhsItem, rhsItem));
                }
            }
            if (lhsItem.getType() == TreeItem.Type.TREE) {
                Tree lhsNextTree = storage.get(lhsItem.getHash(), Tree.class);
                if (rhsItem == null || rhsItem.getType() == TreeItem.Type.FILE) {
                    pairs.add(new TreePair(fullPath, lhsItem, null));
                    walk(storage, pairs, fullPath, lhsNextTree, Tree.EMPTY);
                }
                if (rhsItem != null && rhsItem.getType() == TreeItem.Type.TREE) {
                    pairs.add(new TreePair(fullPath, lhsItem, rhsItem)); // TODO: useless?
                    walk(storage, pairs, fullPath, lhsNextTree, storage.get(rhsItem.getHash(), Tree.class));
                }
            }
        }
    }
}
