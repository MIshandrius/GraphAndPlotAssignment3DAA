package daa;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public static long mstWeight(Graph graph){
        return minimumSpanningForest(graph).totalWeight();
    }

    public static KruskalResult minimumSpanningForest(Graph graph){
        return minimumSpanningForest(graph, new Metrics());
    }

    public static KruskalResult minimumSpanningForest(Graph graph, Metrics metrics){
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "MST requires an undirected graph"
            );
        }

        DSU dsu = new DSU(graph.size());

        List<FullEdge> edges = new ArrayList<>(graph.edges());
        edges.sort((a,b) -> {
            metrics.comparisons++;
            return Integer.compare(a.weight(), b.weight());
        });

        long totalWeight = 0;
        int edgeCount = 0;

        for(FullEdge edge : edges){
            metrics.edgeScans++;
            if(dsu.union(edge.from(), edge.to())){
                totalWeight += edge.weight();
                edgeCount++;
                if(edgeCount == graph.size() - 1) break; // connected MST complete; disconnected graphs still produce a forest
            }
        }

        metrics.dsuOps = dsu.getFindCalls() + dsu.getSuccessfulUnions();
        return new KruskalResult(
                totalWeight, edgeCount, dsu.components()
        );


    }


}
