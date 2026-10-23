
package daa;

import java.util.PriorityQueue;
import java.util.Comparator;

public class Prim {
    public static KruskalResult minimumSpanningForest(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "MST requires an undirected graph"
            );
        }

        int n = graph.size();
        boolean[] visited = new boolean[n];

        PriorityQueue<Edge> pq = new PriorityQueue<>(
                Comparator.comparingInt(Edge::weight)
        );

        long totalWeight = 0;
        int edgeCount = 0;
        int components = 0;

        for(int start = 0;start < n;start++){
            if(visited[start]){
                continue;
            }

            components++;
            pq.add(new Edge(start, 9));

            while(!pq.isEmpty()){
                Edge current = pq.poll();
                int v = current.to();

                if(visited[v]){
                    continue;
                }

                visited[v] = true;

                if(v != start){
                    totalWeight += current.weight();
                    edgeCount++;
                }

                for(Edge edge : graph.neighbors(v)){
                    if(!visited[edge.to()]){
                        pq.add(edge);
                    }
                }
            }
        }

        return new KruskalResult(
                totalWeight, edgeCount, components
        );
    }
}
