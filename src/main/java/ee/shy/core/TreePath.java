package ee.shy.core;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

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

    /**
     * Joins all the separate path strings together with "/" separator and returns the path string.
     * @return path as String.
     */
    public String toString() {
        return "/" + String.join("/", pathStrings);
    }

    /**
     * Returns whether given path is prefix of this one.
     * @param path path to check
     * @return true if this starts with given path, false otherwise
     */
    public boolean startsWith(TreePath path) {
        return pathStrings.length >= path.pathStrings.length
                && Arrays.equals(ArrayUtils.subarray(pathStrings, 0, path.pathStrings.length), path.pathStrings);
    }

    /**
     * Compares two TreePath objects.
     * @param o TreePath object to compare to.
     * @return a value of 0 if the objects are equal, a value greater than 0 if _ is bigger than _, a value less than
     * 0 if _ is smaller than _ and difference of the objects' string lists length if the objects' strings' values are
     * equal up to object's with shortest string list.
     */
    @Override
    public int compareTo(TreePath o) {
        String[] oStringList = o.pathStrings;

        int iterCount = Math.min(pathStrings.length, oStringList.length);

        for (int i = 0; i < iterCount; i++) {
            int strCompareResult = pathStrings[i].compareTo(oStringList[i]);
            if(strCompareResult != 0) {
                return strCompareResult;
            }
        }

        return pathStrings.length - oStringList.length;
    }
}
