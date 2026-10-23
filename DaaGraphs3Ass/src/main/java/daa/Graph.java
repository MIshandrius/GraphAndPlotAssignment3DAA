package daa;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;

    private final List<List<Edge>> adj;

    private final List<FullEdge> edges;

    public Graph(int n, boolean directed){
        if(n < 0){
            throw new IllegalArgumentException("Number of vertices cannot be negative");
        }
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        this.edges = new ArrayList<>();

        for(int i=0;i < n;i++){
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight){
        validateVertex(u);
        validateVertex(v);

        adj.get(u).add(new Edge(v, weight));

        edges.add(new FullEdge(u, v ,weight));

        if(!directed){
            adj.get(v).add(new Edge(u,weight));
        }
    }

    public void validateVertex(int vertex){
        if(vertex < 0 || vertex >= n){
            throw new IllegalArgumentException("Invalid vertex");
        }
    }

    public int size(){
        return n;
    }

    public List<Edge> neighbors(int vertex){
        validateVertex(vertex);
        return java.util.Collections.unmodifiableList(
                adj.get(vertex)
        );
    }

    public List<FullEdge> edges(){
        return java.util.Collections.unmodifiableList(edges);
    }

    public boolean isDirected(){
        return directed;
    }





}
