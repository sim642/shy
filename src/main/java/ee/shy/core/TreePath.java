package ee.shy.core;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class TreePath implements Comparable<TreePath> {
    private final String[] pathStrings;

    public TreePath(String[] treePath) {
        this.pathStrings = treePath;
    }

    public TreePath resolve(String str) {
        return new TreePath(ArrayUtils.addAll(pathStrings, ArrayUtils.remove(str.split("/"), 0)));
    }

    public String toString() {
        return String.join("/", pathStrings);
    }

    @Override
    public int compareTo(TreePath o) {
        String[] oStringList = o.pathStrings;

        int iterCount = Math.min(pathStrings.length, oStringList.length);
        
        for (int i = 0; i < iterCount; i++) {
            int strCompareResult = oStringList[i].compareTo(pathStrings[i]);
            if(strCompareResult != 0) {
                return strCompareResult;
            }
        }

        return oStringList.length - pathStrings.length;
    }
}
