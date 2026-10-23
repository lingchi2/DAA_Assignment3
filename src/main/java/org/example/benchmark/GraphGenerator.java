package org.example.benchmark;

import org.example.graph.Graph;
import java.util.*;

public final class GraphGenerator {
    private GraphGenerator() {
    }

    public static Graph generate(int n, int m, String kind, Random rnd) {
        boolean directed = !kind.equals("undirected");
        Graph g = new Graph(n, directed);
        if (kind.equals("undirected")) {
            int[] p = new int[n];
            for (int i = 0; i < n; i++)
                p[i] = i;
            shuffle(p, rnd);
            for (int i = 1; i < n; i++)
                g.addEdge(p[rnd.nextInt(i)], p[i], 1 + rnd.nextInt(1000));
            while (g.edges().size() < m) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n);
                if (a == b)
                    continue;
                g.addEdge(a, b, 1 + rnd.nextInt(1000));
            }
        } else if (kind.equals("directed")) {
            while (g.edges().size() < m) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n);
                if (a == b)
                    continue;
                g.addEdge(a, b, 1 + rnd.nextInt(1000));
            }
        } else {
            int[] rank = new int[n];
            for (int i = 0; i < n; i++)
                rank[i] = i;
            shuffle(rank, rnd);
            while (g.edges().size() < m) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n);
                if (a == b)
                    continue;
                if (rank[a] > rank[b]) {
                    int z = a;
                    a = b;
                    b = z;
                }
                g.addEdge(a, b, 1 + rnd.nextInt(1000));
            }
        }
        return g;
    }

    private static void shuffle(int[] a, Random r) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1), t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }
}
