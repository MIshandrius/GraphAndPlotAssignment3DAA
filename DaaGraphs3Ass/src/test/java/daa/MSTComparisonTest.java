package daa;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class MSTComparisonTest {
    private Graph randomConnectedGraph(int n, Random rnd) {

        Graph graph = new Graph(n, false);

        for (int i = 1; i < n; i++) {
            graph.addEdge(i - 1, i, 1 + rnd.nextInt(100));
        }

        for (int i = 0; i < n * 2; i++) {

            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            if (u != v) {
                graph.addEdge(u, v, 1 + rnd.nextInt(100));
            }
        }

        return graph;
    }

    @Test
    void testPrimEqualsKruskal() {
        Random rnd = new Random(42);

        for (int test = 0; test < 100; test++) {

            int n = 5 + rnd.nextInt(20);

            Graph graph = randomConnectedGraph(n, rnd);

            KruskalResult kruskal =
                    Kruskal.minimumSpanningForest(graph);

            KruskalResult prim =
                    Prim.minimumSpanningForest(graph);

            assertEquals(
                    kruskal.totalWeight(),
                    prim.totalWeight()
            );
        }

    }

}
