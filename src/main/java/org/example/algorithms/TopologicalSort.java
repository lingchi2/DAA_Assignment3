package org.example.algorithms;

import org.example.graph.Graph;
import org.example.graph.Graph.Edge;
import org.example.metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public final class TopologicalSort {
    private TopologicalSort() {
    }

    public static Algorithms.TopoResult kahn(Graph graph, Metrics metrics) {
        int n = graph.size();
        int[] indegree = new int[n];
        for (Edge edge : graph.edges()) {
            indegree[edge.to()]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int vertex = 0; vertex < n; vertex++) {
            if (indegree[vertex] == 0) {
                queue.add(vertex);
            }
        }

        List<Integer> order = new ArrayList<>(n);
        while (!queue.isEmpty()) {
            int vertex = queue.remove();
            order.add(vertex);
            for (Edge edge : graph.outgoing(vertex)) {
                metrics.edgeScans++;
                if (--indegree[edge.to()] == 0) {
                    queue.add(edge.to());
                }
            }
        }
        return new Algorithms.TopoResult(order, order.size() == n);
    }
}
