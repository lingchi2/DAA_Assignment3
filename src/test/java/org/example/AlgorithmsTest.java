package org.example;

import org.example.graph.Graph;
import org.example.metrics.Metrics;
import org.example.algorithms.*;
import org.example.algorithms.Algorithms.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {
    @Test
    void dsuComponents() {
        Metrics m = new Metrics();
        DSU d = new DSU(5, m);
        d.union(0, 1);
        d.union(1, 2);
        d.union(3, 4);
        assertEquals(2, d.components());
        assertEquals(d.find(0), d.find(2));
        assertNotEquals(d.find(0), d.find(3));
    }

    @Test
    void mstAgreementAndDisconnectedForest() {
        Random r = new Random(7);
        for (int z = 0; z < 100; z++) {
            int n = 2 + r.nextInt(20);
            Graph g = new Graph(n, false);
            for (int i = 1; i < n; i++)
                g.addEdge(i, r.nextInt(i), r.nextInt(100));
            for (int i = 0; i < n; i++)
                for (int j = i + 1; j < n; j++)
                    if (r.nextDouble() < .15)
                        g.addEdge(i, j, r.nextInt(100));
            assertEquals(Algorithms.kruskal(g, new Metrics()).weight(), Algorithms.prim(g, new Metrics()).weight());
        }
        Graph d = new Graph(4, false);
        d.addEdge(0, 1, 2);
        d.addEdge(2, 3, 1);
        assertEquals(2, Algorithms.kruskal(d, new Metrics()).components());
        assertEquals(2, Algorithms.prim(d, new Metrics()).components());
    }

    @Test
    void topoDetectsCycle() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 1, 1);
        assertFalse(Algorithms.kahn(g, new Metrics()).acyclic());
    }

    @Test
    void dagPathsWithNegativeWeights() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 2, -3);
        g.addEdge(2, 3, 2);
        PathResult p = Algorithms.dagShortestPaths(g, new Metrics());
        assertEquals(-1, p.distances()[3]);
        assertEquals(List.of(1, 2, 3), Algorithms.restorePath(3, p.parent()));
    }

    @Test
    void sccStackSafe() {
        int n = 100000;
        Graph g = new Graph(n, true);
        for (int i = 0; i < n - 1; i++)
            g.addEdge(i, i + 1, 1);
        assertEquals(n, Algorithms.kosaraju(g, new Metrics()).size());
    }

    @Test
    void sccExpected() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);
        assertEquals(3, Algorithms.kosaraju(g, new Metrics()).size());
    }
}
