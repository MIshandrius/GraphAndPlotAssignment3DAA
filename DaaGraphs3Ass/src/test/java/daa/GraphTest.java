package daa;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class GraphTest {

    @Test
    void testUndirecredGraph(){
        Graph graph = new Graph(3, false);

        graph.addEdge(0,1,10);

        assertEquals(1, graph.neighbors(0).size());
        assertEquals(1, graph.neighbors(1).size());
        assertEquals(0, graph.neighbors(2).size());

    }

    @Test
    void testDirectedGraph(){
        Graph graph = new Graph(3, true);

        graph.addEdge(0,  1, 10);

        assertEquals(1, graph.neighbors(0).size());
        assertEquals(0, graph.neighbors(1).size());
    }

    @Test
    void testInvalidVertex(){
        Graph graph = new Graph(3, false);

        assertThrows(
                IllegalArgumentException.class, () -> graph.addEdge(0, 5,10)
        );
    }

    @Test
    void testEdgeWeight() {
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 25);

        assertEquals(25,graph.neighbors(1).get(0).weight());
    }

    @Test
    void testParallelEdge(){
        Graph graph = new Graph(2, false);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 20);

        assertEquals(2, graph.neighbors(0).size());
    }

    @Test
    void testSelfLoop(){
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 0, 15);

        assertEquals(2, graph.neighbors(0).size());

    }

    @Test
    void testEdgeList(){
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 20);

        assertEquals(2, graph.edges().size());
    }

    @Test
    void testReadGraph() throws IOException{
        Graph graph = graphReader.read(
                "data/connected.txt"
        );

        assertEquals(4, graph.size());
        assertEquals(4,graph.edges().size());
    }

    @Test
    void testDisconnectedGraph() throws IOException{
        Graph graph = graphReader.read(
                "data/disconnected.txt"
        );

        assertEquals(0, graph.neighbors(4).size());
        assertEquals(2, graph.edges().size());
        assertEquals(5, graph.size());

    }

    @Test
    void testCyclicGraph() throws IOException{
        Graph graph = graphReader.read("data/cyclic.txt");

        assertEquals(3, graph.size());
        assertEquals(3, graph.edges().size());
        assertEquals(0, graph.edges().get(2).to());
    }

    @Test
    void testinvalidFile() throws IOException{
        Path file = Files.createTempFile("bad-graph ",".txt");

        try{
            Files.writeString(file, """
                    3 2 undirected
                    0 1 10
                    """);
            assertThrows(IllegalArgumentException.class, () -> graphReader.read(file.toString()));
        }
        finally {
            Files.deleteIfExists(file);
        }
    }

}
