package daa;

public class DagResult {
    private final long[] dist;
    private final int[] parent;
    public DagResult(long[] dist, int[] parent){
        this.dist = dist;
        this.parent = parent;
    }

    public long[] dist(){
        return dist;
    }

    public int[] parent(){
        return parent;
    }
}
