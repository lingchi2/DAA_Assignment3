package org.example.graph;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Graph {
    public record Edge(int from, int to, int weight) {
    }

    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int n, boolean directed) {
        if (n < 0)
            throw new IllegalArgumentException("n must be non-negative");
        this.n = n;
        this.directed = directed;
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            adj.add(new ArrayList<>());
    }

    public int size() {
        return n;
    }

    public boolean directed() {
        return directed;
    }

    public List<Edge> edges() {
        return edges;
    }

    public List<Edge> outgoing(int v) {
        return adj.get(v);
    }

    public void addEdge(int u, int v, int w) {
        check(u);
        check(v);
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        if (!directed && u != v)
            adj.get(v).add(new Edge(v, u, w));
    }

    private void check(int v) {
        if (v < 0 || v >= n)
            throw new IndexOutOfBoundsException("vertex " + v);
    }

    public Graph reverse() {
        Graph r = new Graph(n, true);
        for (Edge e : edges)
            r.addEdge(e.to, e.from, e.weight);
        return r;
    }

    public static Graph read(Path path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            do {
                line = br.readLine();
            } while (line != null && (line.isBlank() || line.stripLeading().startsWith("#")));
            if (line == null)
                throw new IOException("Missing header");
            String[] h = line.trim().split("\s+");
            if (h.length != 3)
                throw new IOException("Header must be: n m directed|undirected");
            int n = Integer.parseInt(h[0]), m = Integer.parseInt(h[1]);
            boolean d = switch (h[2].toLowerCase(Locale.ROOT)) {
                case "directed" -> true;
                case "undirected" -> false;
                default -> throw new IOException("Unknown graph type");
            };
            Graph g = new Graph(n, d);
            for (int i = 0; i < m; i++) {
                line = br.readLine();
                while (line != null && (line.isBlank() || line.stripLeading().startsWith("#")))
                    line = br.readLine();
                if (line == null)
                    throw new IOException("Expected " + m + " edges");
                String[] p = line.trim().split("\s+");
                if (p.length != 3)
                    throw new IOException("Edge line must contain u v w");
                g.addEdge(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
            return g;
        }
    }
}
