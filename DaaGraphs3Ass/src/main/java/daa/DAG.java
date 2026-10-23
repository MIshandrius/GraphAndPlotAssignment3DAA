package daa;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DAG {
    public static DagResult shortestPaths(Graph graph){
        return shortestPaths(graph, new Metrics());
    }
    public static DagResult shortestPaths(Graph graph, Metrics metrics){
        int n = graph.size();
        int[] order = Kahn.topologicalSort(graph);
        long[] dist = new long[n];

        int[] indegree = new int[n];

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        Arrays.fill(dist, Long.MAX_VALUE);

        for(FullEdge edge : graph.edges()){
            indegree[edge.to()]++;
        }

        for(int i = 0;i<n;i++){
            if(indegree[i] == 0){
                dist[i] = 0;
            }
        }

        for(int current : order){
            if(dist[current] == Long.MAX_VALUE){
                continue;
            }

            for(Edge edge : graph.neighbors(current)){
                metrics.edgeScans++;
                long newDist = dist[current] + edge.weight();

                if(newDist < dist[edge.to()]){
                    dist[edge.to()] = newDist;
                    parent[edge.to()] = current;
                }


            }
        }
        return new DagResult(dist, parent);
    }
}
