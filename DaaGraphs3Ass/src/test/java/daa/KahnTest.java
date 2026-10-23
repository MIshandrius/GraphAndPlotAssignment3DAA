package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {
    @Test
    void testTopologicalSort(){
        Graph graph = new Graph(4 , true);

        graph.addEdge(0,1,1);
        graph.addEdge(0,2,1);
        graph.addEdge(1,3,1);
        graph.addEdge(2,3,1);

        int[] order = Kahn.topologicalSort(graph);

        assertEquals(4, order.length);

        int[] pos = new int[4];

        for(int i = 0; i < order.length;i++){
            pos[order[i]] = i;
        }

        for(FullEdge edge : graph.edges()){
            int from = edge.from();
            int to = edge.to();

            assertTrue(pos[from]<pos[to]);
        }
    }

    @Test
    void testCycleDetection(){
        Graph graph = new Graph(3, true);

        graph.addEdge(0,1,1);
        graph.addEdge(1,2,1);
        graph.addEdge(2,0,1);

        assertThrows(IllegalStateException.class, () -> Kahn.topologicalSort(graph));
    }

}
