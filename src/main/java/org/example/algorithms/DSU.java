package org.example.algorithms;

import org.example.metrics.Metrics;

public final class DSU {
    private final int[] parent, rank;
    private int components;
    private final Metrics metrics;

    public DSU(int n, Metrics m) {
        if (n < 0)
            throw new IllegalArgumentException();
        parent = new int[n];
        rank = new int[n];
        components = n;
        metrics = m;
        for (int i = 0; i < n; i++)
            parent[i] = i;
    }

    public int find(int x) {
        metrics.dsuOps++;
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    public boolean union(int a, int b) {
        int x = find(a), y = find(b);
        if (x == y)
            return false;
        if (rank[x] < rank[y])
            parent[x] = y;
        else if (rank[x] > rank[y])
            parent[y] = x;
        else {
            parent[y] = x;
            rank[x]++;
        }
        components--;
        return true;
    }

    public int components() {
        return components;
    }

    public int size() {
        return parent.length;
    }
}
