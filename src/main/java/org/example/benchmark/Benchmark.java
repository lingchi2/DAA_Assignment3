package org.example.benchmark;

import org.example.graph.Graph;
import org.example.algorithms.*;
import org.example.algorithms.Algorithms.*;
import org.example.metrics.Metrics;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public final class Benchmark {
    static final int[] SIZES = { 1000, 3162, 10000, 31623, 100000 };

    public static void main(String[] args) throws Exception {
        Path out = Path.of("results/results.csv");
        Files.createDirectories(out.getParent());
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(out))) {
            w.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");
            for (int n : SIZES)
                for (int factor : new int[] { 4, 20 }) {
                    int m = n * factor;
                    String density = factor == 4 ? "sparse" : "dense";
                    Graph ug = GraphGenerator.generate(n, m, "undirected", new Random(42));
                    Metrics km = new Metrics();
                    long kt = median(() -> Algorithms.kruskal(ug, new Metrics()));
                    MstResult kr = Algorithms.kruskal(ug, km);
                    Metrics pm = new Metrics();
                    long pt = median(() -> Algorithms.prim(ug, new Metrics()));
                    MstResult pr = Algorithms.prim(ug, pm);
                    if (kr.weight() != pr.weight())
                        throw new IllegalStateException("MST mismatch n=" + n + " m=" + m);
                    row(w, "Kruskal", "undirected_" + density, n, m, kt, km, kr.weight());
                    w.flush();
                    row(w, "Prim", "undirected_" + density, n, m, pt, pm, pr.weight());
                    w.flush();
                    Graph dg = GraphGenerator.generate(n, m, "directed", new Random(42));
                    Metrics sm = new Metrics();
                    long st = median(() -> Algorithms.kosaraju(dg, new Metrics()));
                    List<List<Integer>> scc = Algorithms.kosaraju(dg, sm);
                    row(w, "Kosaraju", "directed_" + density, n, m, st, sm, scc.size());
                    w.flush();
                    Graph dag = GraphGenerator.generate(n, m, "dag", new Random(42));
                    Metrics tm = new Metrics();
                    long tt = median(() -> Algorithms.kahn(dag, new Metrics()));
                    TopoResult topo = Algorithms.kahn(dag, tm);
                    if (!topo.acyclic())
                        throw new IllegalStateException("Generated DAG contains cycle");
                    validate(topo.order(), dag);
                    row(w, "Kahn", "dag_" + density, n, m, tt, tm, topo.order().size());
                    w.flush();
                    Metrics dm = new Metrics();
                    long dt = median(() -> Algorithms.dagShortestPaths(dag, new Metrics()));
                    PathResult paths = Algorithms.dagShortestPaths(dag, dm);
                    row(w, "DagShortestPath", "dag_" + density, n, m, dt, dm, paths.sum());
                    w.flush();
                    System.out.printf(Locale.ROOT, "n=%d m=%d mst=%d scc=%d dagDistanceSum=%d%n", n, m, kr.weight(),
                            scc.size(), paths.sum());
                }
        }
    }

    interface Task {
        Object run();
    }

    static long median(Task task) {
        long[] a = new long[5];
        for (int i = 0; i < 5; i++) {
            long s = System.nanoTime();
            task.run();
            a[i] = (System.nanoTime() - s) / 1_000_000;
        }
        Arrays.sort(a);
        return a[2];
    }

    static void row(PrintWriter w, String alg, String graph, int n, int m, long time, Metrics x, long result) {
        w.printf(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n", alg, graph, n, m, time, x.dsuOps, x.edgeScans,
                x.comparisons, x.maxDepth, result);
    }

    static void validate(List<Integer> order, Graph g) {
        int[] p = new int[g.size()];
        for (int i = 0; i < order.size(); i++)
            p[order.get(i)] = i;
        for (Graph.Edge e : g.edges())
            if (p[e.from()] >= p[e.to()])
                throw new IllegalStateException("Invalid topological order");
    }
}
