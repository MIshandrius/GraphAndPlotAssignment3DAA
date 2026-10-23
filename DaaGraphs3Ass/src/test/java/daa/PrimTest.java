package daa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimTest {
    @Test
    void testDisconnectedGraph() {
        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(2, 3, 7);

        KruskalResult result =
                Prim.minimumSpanningForest(graph);

        assertEquals(11, result.totalWeight());
        assertEquals(2, result.edgeCount());
        assertEquals(3, result.components());
    }


}
