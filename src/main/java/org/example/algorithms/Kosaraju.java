package org.example.algorithms;

import org.example.graph.Graph;
import org.example.graph.Graph.Edge;
import org.example.metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Kosaraju {
    private Kosaraju() {
    }

    public static List<List<Integer>> run(Graph graph, Metrics metrics) {
        int n = graph.size();
        boolean[] seen = new boolean[n];
        int[] next = new int[n];
        List<Integer> finish = new ArrayList<>(n);
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        for (int start = 0; start < n; start++) {
            if (seen[start]) {
                continue;
            }
            seen[start] = true;
            stack.push(start);
            while (!stack.isEmpty()) {
                metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());
                int vertex = stack.peek();
                List<Edge> outgoing = graph.outgoing(vertex);
                if (next[vertex] < outgoing.size()) {
                    Edge edge = outgoing.get(next[vertex]++);
                    metrics.edgeScans++;
                    if (!seen[edge.to()]) {
                        seen[edge.to()] = true;
                        stack.push(edge.to());
                    }
                } else {
                    stack.pop();
                    finish.add(vertex);
                }
            }
        }

        Graph reversed = graph.reverse();
        Arrays.fill(seen, false);
        List<List<Integer>> components = new ArrayList<>();
        for (int i = finish.size() - 1; i >= 0; i--) {
            int start = finish.get(i);
            if (seen[start]) {
                continue;
            }
            List<Integer> component = new ArrayList<>();
            stack.push(start);
            seen[start] = true;
            while (!stack.isEmpty()) {
                metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());
                int vertex = stack.pop();
                component.add(vertex);
                for (Edge edge : reversed.outgoing(vertex)) {
                    metrics.edgeScans++;
                    if (!seen[edge.to()]) {
                        seen[edge.to()] = true;
                        stack.push(edge.to());
                    }
                }
            }
            components.add(component);
        }
        return components;
    }
}
