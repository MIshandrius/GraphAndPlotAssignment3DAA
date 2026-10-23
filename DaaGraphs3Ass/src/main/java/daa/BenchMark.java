package daa;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class BenchMark {

    public static void main(String[] args) {

        int[] sizes = {1000, 3162, 10000, 31623, 100000};

        int[] densities = {4, 20};

        for(int i = 0; i < sizes.length;i++){
            for(int j = 0; j < densities.length; j++){

                int n = sizes[i];
                int m = n * densities[j];

                Graph mstGraph = randomGraph(n, m, false);
                Graph sccGraph = randomGraph(n, m, true);
                Graph dagGraph = randomDAG(n, m);

                long kruskalWeight = Kruskal.mstWeight(mstGraph);
                long primWeight = Prim.minimumSpanningForest(mstGraph).totalWeight();

                if (kruskalWeight != primWeight) {
                    throw new IllegalStateException("MST weights do not match!");
                }

                int components = Kosaraju.countSCC(sccGraph);

                int[] order = Kahn.topologicalSort(dagGraph);

                DagResult result = DAG.shortestPaths(dagGraph);

                long distanceSum = 0;

                for (long distance : result.dist()) {
                    distanceSum += distance;
                }

                System.out.println("MST: " + kruskalWeight);
                System.out.println("SCC: " + components);
                System.out.println("DAG order: " + order.length);
                System.out.println("DAG sum: " + distanceSum);

                System.out.println("Generated n=" + n + ", m=" + m);
            }
        }
    }

    private static int[] shuffledVertices(int n, Random rnd) {
        int[] vertices = new int[n];

        for (int i = 0; i < n; i++) {
            vertices[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);

            int temp = vertices[i];
            vertices[i] = vertices[j];
            vertices[j] = temp;
        }

        return vertices;
    }

    private static Graph randomGraph(int n, int m, boolean directed) {
        Graph graph = new Graph(n, directed);
        Random rnd = new Random(42);

        if (!directed) {
            int[] perm = shuffledVertices(n, rnd);

            // Make sure the graph is connected
            for (int i = 1; i < n; i++) {
                int u = perm[rnd.nextInt(i)];
                int v = perm[i];
                int weight = 1 + rnd.nextInt(1000);

                graph.addEdge(u, v, weight);
            }
        }

        int existing = directed ? 0 : n - 1;

        for (int i = existing; i < m; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            while (u == v) {
                v = rnd.nextInt(n);
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        return graph;
    }

    private static Graph randomDAG(int n, int m) {
        Graph graph = new Graph(n, true);
        Random rnd = new Random(42);

        int[] rank = shuffledVertices(n, rnd);

        for (int i = 0; i < m; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            while (u == v) {
                v = rnd.nextInt(n);
            }

            if (rank[u] > rank[v]) {
                int temp = u;
                u = v;
                v = temp;
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        return graph;
    }



}