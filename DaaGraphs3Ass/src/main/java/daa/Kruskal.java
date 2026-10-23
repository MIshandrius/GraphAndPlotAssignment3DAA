package daa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public static long mstWeight(Graph graph){
        return minimumSpanningForest(graph).totalWeight();
    }

    public static KruskalResult minimumSpanningForest(Graph graph, Metrics metrics) {
        return minimumSpanningForest(graph, new Metrics());
    }

    public static KruskalResult minimumSpanningForest(Graph graph){
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "MST requires an undirected graph"
            );
        }

        DSU dsu = new DSU(graph.size());

        List<FullEdge> edges = new ArrayList<>(graph.edges());
        edges.sort(Comparator.comparingInt(FullEdge::weight));

        long totalWeight = 0;
        int edgeCount = 0;

        for(FullEdge edge : edges){
            if(dsu.union(edge.from(), edge.to())){
                totalWeight += edge.weight();
                edgeCount++;
            }
        }

        return new KruskalResult(
                totalWeight, edgeCount, dsu.components()
        );


    }


}
