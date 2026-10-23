package daa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KruskalTest {
    private Graph randomConnectedGraph(int n, Random rnd){
        Graph graph = new Graph(n, false);

        for(int i = 1; i < n; i++){
            int previous = rnd.nextInt(i);
            int weight = 1 + rnd.nextInt(100);

            graph.addEdge(previous, i, weight);
        }
        for(int i = 0; i < n; i++){
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            int weight = 1+ rnd.nextInt(100);

            if(a != b){
                graph.addEdge(a,b,weight);
            }
        }

        return graph;
    }

    @Test
    void testSimpleMST(){
        Graph graph = new Graph(33, false);

        graph.addEdge(0,1,4);
        graph.addEdge(1,2,2);
        graph.addEdge(0,2, 10);

        long result = Kruskal.mstWeight(graph);

        assertEquals(6, result);

    }

    @Test
    void testDisconnectedGraph(){
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 5);
        graph.addEdge(2,3,7);

        KruskalResult result = Kruskal.minimumSpanningForest(graph);

        assertEquals(12, result.totalWeight());
        assertEquals(2, result.edgeCount());
        assertEquals(2, result.components());
    }

    @Test
    void testCycle(){
        Graph graph = new Graph(3, false);

        graph.addEdge(0,1,2);
        graph.addEdge(1,2,3);
        graph.addEdge(0,2,10);

        KruskalResult result = Kruskal.minimumSpanningForest(graph);

        assertEquals(5, result.totalWeight());
        assertEquals(2, result.edgeCount());
        assertEquals(1,result.components());
    }

    @Test
    void testRandomConnectedGraphs(){
        Random rnd = new Random(42);

        for(int test = 0; test < 100; test++ ){
            int n = 5 + rnd.nextInt(20);

            Graph graph = randomConnectedGraph(n, rnd);

            KruskalResult result = Kruskal.minimumSpanningForest(graph);

            assertEquals(n-1, result.edgeCount());
            assertEquals(1, result.components());
        }
    }



}
