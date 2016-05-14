package ee.shy.core;

import java.util.ArrayList;
import java.util.List;

public class TreePath implements Comparable<TreePath> {
    private final List<String> pathStrings = new ArrayList<>();

    TreePath(String stringPath) {
        for(String s : stringPath.split("/"))
            pathStrings.add(s);
    }

    public void resolve(String str) {
        new TreePath(this.toString() + str);
    }

    List<String> getPathStrings() {
        return pathStrings;
    }

    public String toString() {
        String stringPath = "";
        for (String s : pathStrings) {
            stringPath += "/" + s;
        }
        return stringPath;
    }

    @Override
    public int compareTo(TreePath o) {
        List<String> oStringList = o.getPathStrings();
        if(oStringList.size() < pathStrings.size()) {
            return 1;
        }
        else if(oStringList.size() > pathStrings.size()) {
            return -1;
        }

        int iterCount = Math.min(pathStrings.size(), oStringList.size());
        
        for(int i = 0; i < iterCount; i++) {
            int strCompareResult = oStringList.get(i).compareTo(pathStrings.get(i));
            if(strCompareResult != 0) {
                return strCompareResult;
            }
        }
        return 0;
    }
}
