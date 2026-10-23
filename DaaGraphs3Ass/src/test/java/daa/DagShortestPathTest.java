package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {
    @Test
    void testNegativeWeight(){
        Graph graph = new Graph(3, true);

        graph.addEdge(0,1,5);
        graph.addEdge(1,2,-8);
        graph.addEdge(0,2,2);

        DagResult result = DAG.shortestPaths(graph);

        long[] dist = result.dist();

        assertEquals(-3, dist[2]);
    }

    @Test
    void testMultipleSources(){
        Graph graph = new Graph(3,true);

        graph.addEdge(0,2,4);
        graph.addEdge(1,2, 2);

        DagResult result = DAG.shortestPaths(graph);

        long[] dist = result.dist();
        int[] parent = result.parent();

        assertEquals(1, parent[2]);

        assertEquals(2, dist[2]);
        assertEquals(0, dist[0]);
        assertEquals(0, dist[1]);
    }

    @Test
    void testCycle(){
        Graph graph = new Graph(3,true);

        graph.addEdge(0,1, 2);
        graph.addEdge(1,2, 2);
        graph.addEdge(2,0, 2);

        assertThrows(IllegalStateException.class ,() -> DAG.shortestPaths(graph));
    }
}
