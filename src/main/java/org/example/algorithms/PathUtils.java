package org.example.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PathUtils {
    private PathUtils() {
    }

    public static List<Integer> restore(int target, int[] parent) {
        ArrayList<Integer> path = new ArrayList<>();
        for (int vertex = target; vertex != -1; vertex = parent[vertex]) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }
}
