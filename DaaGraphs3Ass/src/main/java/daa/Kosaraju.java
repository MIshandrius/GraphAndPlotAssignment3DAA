package daa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Kosaraju {
    public static int countSCC(Graph graph){
        int n = graph.size();
        boolean[] visited = new boolean[n];

        int[] next = new int[n];

        ArrayList<Integer> order = new ArrayList<>();

        if(!graph.isDirected()){
            throw new IllegalArgumentException("Kosaraju requires directed graph");
        }

        for(int i = 0; i < n; i++){
            if(!visited[i]){
                ArrayDeque<Integer> stack = new ArrayDeque<>();

                stack.push(i);

                visited[i] = true;

                while(!stack.isEmpty()){
                    int current = stack.peek();

                    if(next[current] < graph.neighbors(current).size()){
                        Edge edge = graph.neighbors(current).get(next[current]);
                        next[current]++;

                        if(!visited[edge.to()]){
                            stack.push(edge.to());
                            visited[edge.to()] = true;
                        }
                    }
                    else{
                        stack.pop();
                        order.add(current);
                    }

                }
            }
        }

        Graph reversed = new Graph(n, true);

        for(FullEdge edge : graph.edges()){
            reversed.addEdge(edge.to(), edge.from(), edge.weight());
        }

        Arrays.fill(visited,false);

        int components = 0;

        for(int i = order.size() - 1; i >= 0; i--){
            int current = order.get(i);

            if(!visited[current]){
                ArrayDeque<Integer> stack = new ArrayDeque<>();
                stack.push(current);

                visited[current] = true;
                components++;


                while(!stack.isEmpty()){
                    int vertex = stack.pop();

                    for(Edge edge : reversed.neighbors(vertex)){
                        if(!visited[edge.to()]){
                            visited[edge.to()] = true;
                            stack.push(edge.to());
                        }
                    }

                }
            }



        }


        return components;
    }
}
