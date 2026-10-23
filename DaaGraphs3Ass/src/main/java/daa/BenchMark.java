package daa;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;

public class BenchMark {
    private static final int[] SIZES = {1000, 3162, 10000, 31623, 100000};
    private static final int[] DENSITIES = {4, 20};
    private static final long[] MST = {147970,30363,468911,97589,1486139,309550,4715515,968517,15012094,3056681};
    private static final int[] SCC = {27,1,137,1,373,1,1188,1,3923,1};
    private static final long[] DAG_SUM = {396274,252411,1271911,723157,3941828,2381342,12551891,7376612,40096539,23512087};

    interface Measured { long run(Metrics metrics); }
    static class Row {
        String algorithm, graph; int n,m; double time; Metrics metrics; long result;
        Row(String a,String g,int n,int m,double t,Metrics x,long r){algorithm=a;graph=g;this.n=n;this.m=m;time=t;metrics=x;result=r;}
        String csv(){return String.format(Locale.US,"%s,%s,%d,%d,%.4f,%d,%d,%d,%d,%d",algorithm,graph,n,m,time,metrics.dsuOps,metrics.edgeScans,metrics.comparisons,metrics.maxDepth,result);}
    }
    private static Row measure(String name,String kind,int n,int m,Measured operation){
        double[] times = new double[5]; long expected = Long.MIN_VALUE; Metrics first=null;
        for(int run=0;run<5;run++){
            Metrics metrics=new Metrics(); long start=System.nanoTime();
            long value=operation.run(metrics); double elapsed=(System.nanoTime()-start)/1_000_000.0;
            if(expected!=Long.MIN_VALUE && value!=expected) throw new IllegalStateException(name+" inconsistent runs");
            expected=value; times[run]=elapsed; if(first==null)first=metrics;
        }
        Arrays.sort(times);
        return new Row(name,kind,n,m,times[2],first,expected);
    }
    private static void verify(long actual,long expected,String name,int n,int m){
        if(actual!=expected)throw new IllegalStateException(name+" at "+n+"/"+m+": expected "+expected+", got "+actual);
    }
    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("results"));
        try(PrintWriter out=new PrintWriter(Files.newBufferedWriter(Path.of("results/results.csv")))){
            out.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result"); out.flush();
            for(int i=0;i<SIZES.length;i++)for(int j=0;j<DENSITIES.length;j++){
                int n=SIZES[i], m=n*DENSITIES[j], k=i*2+j;
                String density=j==0?"sparse":"dense";
                System.out.println("Starting "+n+" / "+m+" ("+density+")");
                // Process one graph kind at a time to limit memory use.
                Graph mst=randomGraph(n,m,false);
                Row kr=measure("Kruskal",density,n,m,x->Kruskal.minimumSpanningForest(mst,x).totalWeight());
                Row pr=measure("Prim",density,n,m,x->Prim.minimumSpanningForest(mst,x).totalWeight());
                verify(kr.result,MST[k],"Kruskal",n,m); verify(pr.result,kr.result,"Prim",n,m);
                out.println(kr.csv());out.println(pr.csv());out.flush();
                Graph directed=randomGraph(n,m,true);
                Row ko=measure("Kosaraju",density,n,m,x->Kosaraju.countSCC(directed,x));
                verify(ko.result,SCC[k],"Kosaraju",n,m);
                if(ko.metrics.edgeScans!=2L*m)throw new IllegalStateException("Kosaraju scans mismatch");
                out.println(ko.csv());out.flush();
                Graph dag=randomDAG(n,m);
                Row ka=measure("Kahn",density,n,m,x->Kahn.topologicalSort(dag,x).length);
                Row sp=measure("DagShortestPath",density,n,m,x->{long sum=0;for(long d:DAG.shortestPaths(dag,x).dist())sum+=d;return sum;});
                verify(ka.result,n,"Kahn",n,m);verify(sp.result,DAG_SUM[k],"DAG",n,m);
                if(ka.metrics.edgeScans!=m||sp.metrics.edgeScans!=m)throw new IllegalStateException("DAG scan mismatch");
                out.println(ka.csv());out.println(sp.csv());out.flush();
                System.out.println("Verified "+n+" / "+m+"; rows saved.");
            }
        }
        System.out.println("All 50 rows verified in results/results.csv");
    }
    private static int[] shuffledVertices(int n,Random rnd){
        int[] a=new int[n];for(int i=0;i<n;i++)a[i]=i;
        for(int i=n-1;i>0;i--){int j=rnd.nextInt(i+1);int t=a[i];a[i]=a[j];a[j]=t;}return a;
    }
    private static Graph randomGraph(int n,int m,boolean directed){
        Graph g=new Graph(n,directed);Random rnd=new Random(42);
        if(!directed){int[] perm=shuffledVertices(n,rnd);
            for(int i=1;i<n;i++)g.addEdge(perm[rnd.nextInt(i)],perm[i],1+rnd.nextInt(1000));}
        for(int i=directed?0:n-1;i<m;i++){
            int u=rnd.nextInt(n),v=rnd.nextInt(n);while(v==u)v=rnd.nextInt(n);
            g.addEdge(u,v,1+rnd.nextInt(1000));}
        return g;
    }
    private static Graph randomDAG(int n,int m){
        Graph g=new Graph(n,true);Random rnd=new Random(42);int[] rank=shuffledVertices(n,rnd);
        for(int i=0;i<m;i++){
            int u=rnd.nextInt(n),v=rnd.nextInt(n);while(v==u)v=rnd.nextInt(n);
            if(rank[u]>rank[v]){int t=u;u=v;v=t;}
            g.addEdge(u,v,1+rnd.nextInt(1000));}
        return g;
    }
}
