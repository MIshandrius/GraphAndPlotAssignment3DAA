package daa;

import java.util.ArrayDeque;

public class Kahn {
    public static int[] topologicalSort(Graph graph){
        return topologicalSort(graph, new Metrics());
    }
    public static int[] topologicalSort(Graph graph, Metrics metrics){
        int n = graph.size();
        int[] indegree = new int[n];

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        int[] order = new int[n];
        int count = 0;

        for(FullEdge edge : graph.edges()){
            indegree[edge.to()]++;
        }

        for(int i = 0; i < n;i++){

            if(indegree[i]==0){
                queue.add(i);
            }

        }

        while(!queue.isEmpty()){
            int current = queue.remove();

            order[count] = current;
            count++;

            for(Edge edge : graph.neighbors(current)){
                metrics.edgeScans++;
                indegree[edge.to()]--;

                if(indegree[edge.to()] == 0){
                    queue.add(edge.to());
                }
            }
        }

        if(count < n){
            throw new IllegalStateException("graph contains a cycle");
        }

        return order;






    }
}
