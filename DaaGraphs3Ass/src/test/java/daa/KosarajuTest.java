package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KosarajuTest {

    @Test
    void testTwoComponents() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        int components = Kosaraju.countSCC(graph);

        assertEquals(2, components);
    }

    @Test
    void testComponentsCount(){
        Graph graph = new Graph(4, true);

        int components = Kosaraju.countSCC(graph);

        assertEquals(4, components);
    }

    @Test
    void testLargeGraph() {
        int n = 100000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        int components = Kosaraju.countSCC(graph);

        assertEquals(n, components);
    }
}