package org.example.algorithms;

import org.example.graph.Graph;
import org.example.graph.Graph.Edge;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public final class Kruskal {
    private Kruskal() {
    }

    public static Algorithms.MstResult run(Graph graph, Metrics metrics) {
        List<Edge> edges = new ArrayList<>(graph.edges());
        edges.sort((a, b) -> {
            metrics.comparisons++;
            return Integer.compare(a.weight(), b.weight());
        });

        DSU dsu = new DSU(graph.size(), metrics);
        List<Edge> chosen = new ArrayList<>();
        long total = 0;
        for (Edge edge : edges) {
            metrics.edgeScans++;
            if (dsu.find(edge.from()) != dsu.find(edge.to())) {
                dsu.union(edge.from(), edge.to());
                chosen.add(edge);
                total += edge.weight();
            }
        }
        return new Algorithms.MstResult(total, chosen, dsu.components());
    }
}
