package org.example.algorithms;

import org.example.graph.Graph;
import org.example.graph.Graph.Edge;
import org.example.metrics.Metrics;

import java.util.Arrays;

public final class DagShortestPaths {
    private DagShortestPaths() {
    }

    public static Algorithms.PathResult run(Graph graph, Metrics metrics) {
        Algorithms.TopoResult topological = TopologicalSort.kahn(graph, new Metrics());
        if (!topological.acyclic()) {
            throw new IllegalArgumentException("DAG shortest paths require an acyclic graph");
        }

        int n = graph.size();
        long infinity = Long.MAX_VALUE / 4;
        long[] distances = new long[n];
        Arrays.fill(distances, infinity);
        int[] indegree = new int[n];
        for (Edge edge : graph.edges()) {
            indegree[edge.to()]++;
        }
        for (int vertex = 0; vertex < n; vertex++) {
            if (indegree[vertex] == 0) {
                distances[vertex] = 0;
            }
        }

        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        for (int vertex : topological.order()) {
            if (distances[vertex] == infinity) {
                continue;
            }
            for (Edge edge : graph.outgoing(vertex)) {
                metrics.edgeScans++;
                long candidate = distances[vertex] + edge.weight();
                metrics.comparisons++;
                if (candidate < distances[edge.to()]) {
                    distances[edge.to()] = candidate;
                    parent[edge.to()] = vertex;
                }
            }
        }

        long sum = 0;
        for (long distance : distances) {
            if (distance != infinity) {
                sum += distance;
            }
        }
        return new Algorithms.PathResult(distances, parent, sum);
    }
}
