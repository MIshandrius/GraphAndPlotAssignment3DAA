package daa;

public class DSU {
    private final int[] parent;
    private final int[] size;
    private int components;

    private long findCalls;
    private long successfulUnions;

    public DSU(int n){
        parent = new int[n];
        size = new int[n];
        components = n;

        for(int i = 0; i< n;i++){
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x){
        findCalls++;
        int root = x;

        while (parent[root] != root){
            root = parent[root];
        }

        while (parent[x] != x){
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    public boolean union(int a, int b){
        int rootA = find(a);
        int rootB = find(b);

        if(rootA == rootB){
            return false;
        }

        if(size[rootA] < size[rootB]){
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        components--;

        successfulUnions++;

        return true;

    }

    public int components(){
        return components;
    }

    public long getFindCalls(){
        return findCalls;
    }

    public long getSuccessfulUnions(){
        return successfulUnions;
    }
}
