package ee.shy.core;

import ee.shy.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A class that handles trees' paths.
 */
public class TreePath implements Comparable<TreePath> {
    private final String[] pathStrings;

    /**
     * Constructor to assign new array of path directories to new path.
     * @param treePath array of new path's directories.
     */
    private TreePath(String[] treePath) {
        this.pathStrings = treePath;
    }

    /**
     * Constructor to create new empty path.
     */
    public TreePath() {
        this.pathStrings = new String[0];
    }

    /**
     * Adds given String to end of current path
     * @param str String of directory that to add to path.
     * @return new TreePath object with given directory added to path.
     */
    public TreePath resolve(String str) {
        return new TreePath(ArrayUtils.addAll(pathStrings, str.split("/")));
    }

    public String[] getPathStrings() {
        return pathStrings;
    }

    /**
     * Joins all the separate path strings together with "/" separator and returns the path string.
     * @return path as String.
     */
    public String toString() {
        return "/" + String.join("/", (CharSequence[]) pathStrings);
    }

    /**
     * Returns whether given path is prefix of this one.
     * @param prefix path to check
     * @return true if this starts with given path, false otherwise
     */
    public boolean startsWith(TreePath prefix) {
        return CollectionUtils.startsWith(pathStrings, prefix.pathStrings);
    }

    /**
     * Compares two TreePath objects.
     * @param other TreePath object to compare to.
     * @return a value of 0 if the objects are equal, a value greater than 0 if _ is bigger than _, a value less than
     * 0 if _ is smaller than _ and difference of the objects' string lists length if the objects' strings' values are
     * equal up to object's with shortest string list.
     */
    @Override
    public int compareTo(TreePath other) {
        return CollectionUtils.compare(pathStrings, other.pathStrings);
    }
}
