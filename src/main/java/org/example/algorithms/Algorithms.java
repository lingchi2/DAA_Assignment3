package org.example.algorithms;

import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.List;

public final class Algorithms {
    private Algorithms() {
    }

    public record MstResult(long weight, List<Graph.Edge> edges, int components) {
    }

    public record TopoResult(List<Integer> order, boolean acyclic) {
    }

    public record PathResult(long[] distances, int[] parent, long sum) {
    }

    public static MstResult kruskal(Graph graph, Metrics metrics) {
        return Kruskal.run(graph, metrics);
    }

    public static MstResult prim(Graph graph, Metrics metrics) {
        return Prim.run(graph, metrics);
    }

    public static List<List<Integer>> kosaraju(Graph graph, Metrics metrics) {
        return Kosaraju.run(graph, metrics);
    }

    public static TopoResult kahn(Graph graph, Metrics metrics) {
        return TopologicalSort.kahn(graph, metrics);
    }

    public static PathResult dagShortestPaths(Graph graph, Metrics metrics) {
        return DagShortestPaths.run(graph, metrics);
    }

    public static List<Integer> restorePath(int target, int[] parent) {
        return PathUtils.restore(target, parent);
    }
}
