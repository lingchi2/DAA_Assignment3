package org.example.algorithms;

import org.example.graph.Graph;
import org.example.graph.Graph.Edge;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public final class Prim {
    private Prim() {
    }

    public static Algorithms.MstResult run(Graph graph, Metrics metrics) {
        int n = graph.size();
        boolean[] used = new boolean[n];
        record QueueEntry(int vertex, int parent, int weight) {
        }
        PriorityQueue<QueueEntry> queue =
                new PriorityQueue<>(Comparator.comparingInt(QueueEntry::weight));
        List<Edge> chosen = new ArrayList<>();
        long total = 0;
        int components = 0;

        for (int start = 0; start < n; start++) {
            if (used[start]) {
                continue;
            }
            components++;
            queue.add(new QueueEntry(start, -1, 0));
            while (!queue.isEmpty()) {
                QueueEntry entry = queue.poll();
                if (used[entry.vertex()]) {
                    continue;
                }
                used[entry.vertex()] = true;
                if (entry.parent() >= 0) {
                    chosen.add(new Edge(entry.parent(), entry.vertex(), entry.weight()));
                    total += entry.weight();
                }
                for (Edge edge : graph.outgoing(entry.vertex())) {
                    metrics.edgeScans++;
                    if (!used[edge.to()]) {
                        queue.add(new QueueEntry(edge.to(), entry.vertex(), edge.weight()));
                    }
                }
            }
        }
        return new Algorithms.MstResult(total, chosen, components);
    }
}
