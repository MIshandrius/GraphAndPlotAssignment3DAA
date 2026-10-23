# Assignment 3 — Graph Algorithms

**Student:** Ziyakhmetova Zhasmina
**Group:** SE-2509
**Course:** Design and Analysis of Algorithms
**Instructor:** Taubakabyl Nurlybek


# Assignment 3 — Graph Algorithms: DSU, MST, SCC, Topological Sorting and DAG Shortest Paths

## 1. Overview

This project implements a shared adjacency-list `Graph` and six algorithmic components: DSU, Kruskal, Prim, Kosaraju, Kahn and DAG shortest paths. It uses Java collections for storage and explicit stacks for DFS. Graphs may be directed or undirected and may contain isolated vertices, parallel edges or self-loops. Benchmark input is generated deterministically using `Random(42)`.

## 2. Complexity and choice of algorithm

| Algorithm | Time | Extra space | Explanation |
|---|---|---|---|
| DSU find/union | amortized O(α(V)) | O(V) | Path compression and union by size keep the forest shallow. |
| Kruskal | O(E log E + E α(V)) | O(V+E) | Sort edges then test connectivity with DSU. |
| Prim (priority queue) | O(E log E) | O(V+E) | Candidate edges are inserted/removed from a binary heap. |
| Kosaraju | O(V+E) | O(V+E) | Two iterative DFS traversals and a reversed adjacency list. |
| Kahn | O(V+E) | O(V) | Indegrees and a queue process every vertex and edge. |
| DAG shortest paths | O(V+E) | O(V) excluding the shared graph | Topological order followed by one relaxation pass. |

Kruskal fits an undirected weighted edge list, especially when edges are already sorted or sorting is acceptable. Prim fits adjacency lists and grows a forest from visited vertices; its implementation uses a priority queue. Kosaraju is for SCCs of directed graphs, including cyclic graphs. Kahn is for dependency ordering in DAGs and detects cycles. DAG shortest paths is preferable to general shortest-path algorithms when the graph is acyclic; negative edges are allowed.

## 3. Correctness arguments

### Minimum spanning tree: cut property

For any cut dividing a connected weighted graph into two vertex sets, a minimum-weight edge crossing that cut is safe to add to some minimum spanning tree. Kruskal processes edges in nondecreasing weight order. Whenever DSU reports that the endpoints are in different components, the edge crosses a cut between those components. Because lower-weight candidate edges have already been considered, adding this edge preserves the existence of an optimal spanning tree. Prim instead maintains a visited set and chooses the lightest available crossing edge from its priority queue. The same cut property justifies each new edge selected by Prim. Both algorithms therefore produce an MST of the same total weight on connected inputs and a minimum spanning forest on disconnected inputs.

### DAG shortest paths: induction over topological order

Kahn returns an order in which every directed edge goes from an earlier vertex to a later one. Each original zero-indegree vertex begins at distance zero, representing a virtual super-source. Assume that, before processing a vertex, the best distances to all earlier vertices have been established. Every predecessor of the current vertex is earlier in topological order, so its outgoing edge has already been relaxed. Consequently the current distance is the minimum over all possible incoming paths from any source. Relaxing its outgoing edges extends this invariant to later vertices. By induction, after one pass, every vertex has its shortest distance; the parent array records the predecessor that last improved each distance. Negative edge weights do not invalidate this argument because a DAG has no directed cycles and hence no negative cycles.

## 4. Benchmark method and reproducibility

The benchmark uses five sizes (1,000; 3,162; 10,000; 31,623; 100,000), two assigned densities (m=4n and m=20n), and three kinds of generated graphs per configuration. Undirected MST graphs begin with a random spanning tree. Directed SCC graphs use random directed edges. DAG edges are oriented according to a shuffled rank array. Each graph kind uses a new `Random(42)` to match the assignment reference values. Five timed executions are collected per algorithm/configuration, and the median is saved to `results/results.csv` (50 data rows). Counters are incremented in the algorithm bodies; DSU counters come from its internal find and successful union totals. Timings include algorithm work, not graph generation. Java/JVM and hardware affect timings, so counters are the primary reproducible evidence.

The implementation self-checks both MST weights, all provided MST/SCC/DAG reference results, and the exact traversal scan invariants. All ten configurations passed these checks during the included benchmark run.

## 5. Measured results and counters

The following table uses the CSV bundled with this submission. Time values are median milliseconds measured in the build environment and will differ on another machine.

| n | Density | Kruskal MST | SCCs | DAG distance sum | Kruskal median ms | DAG median ms |
|---:|---|---:|---:|---:|---:|---:|
| 1,000 | sparse | 147,970 | 27 | 396,274 | 2.84 | 2.27 |
| 1,000 | dense | 30,363 | 1 | 252,411 | 7.51 | 1.90 |
| 3,162 | sparse | 468,911 | 137 | 1,271,911 | 3.87 | 1.89 |
| 3,162 | dense | 97,589 | 1 | 723,157 | 19.14 | 1.32 |
| 10,000 | sparse | 1,486,139 | 373 | 3,941,828 | 9.14 | 1.54 |
| 10,000 | dense | 309,550 | 1 | 2,381,342 | 47.26 | 6.08 |
| 31,623 | sparse | 4,715,515 | 1,188 | 12,551,891 | 26.52 | 6.26 |
| 31,623 | dense | 968,517 | 1 | 7,376,612 | 121.43 | 58.18 |
| 100,000 | sparse | 15,012,094 | 3,923 | 40,096,539 | 80.91 | 56.82 |
| 100,000 | dense | 3,056,681 | 1 | 23,512,087 | 413.75 | 214.74 |

### Counters divided by growth formulas

Kosaraju scans exactly `2m` edges (one pass over the original adjacency lists and one over the reversed lists). Kahn and DAG relaxation each scan exactly `m` edges, so their `edgeScans/m` ratio is 1. Kruskal counts sorting weight comparisons and stops scanning once its connected MST has `n-1` selected edges. Its `dsuOps/m` and scanned-edge fraction are therefore data-dependent but bounded by a constant multiple of m. Prim counts adjacency scans and priority-queue weight comparisons. In the undirected adjacency-list representation, every stored input edge appears twice, so its `edgeScans/m` is 2.

| Algorithm | Sparse scan slope | Dense scan slope | Representative scan ratio |
|---|---:|---:|---|
| Kruskal | 1.06 | 1.12 | 0.770 (n=1000, sparse) |
| Prim | 1.00 | 1.00 | 2.000 (n=1000, sparse) |
| Kosaraju | 1.00 | 1.00 | 2.000 (n=1000, sparse) |
| Kahn | 1.00 | 1.00 | 1.000 (n=1000, sparse) |
| DagShortestPath | 1.00 | 1.00 | 1.000 (n=1000, sparse) |

The log-log slopes above are least-squares fits of `log(edgeScans)` against `log(n)` at fixed density. Traversal slopes should be 1.00. Kruskal's edge-scan slope need not reveal its sorting log factor: sorting comparisons are tracked separately, while edgeScans only measures the DSU edge loop. This distinction matters when interpreting the operation plot.

### Plots

![Counted operations vs n](results/plots/operations.png)

![Edge scans per input edge](results/plots/scans_per_edge.png)

![Kruskal scanned-edge share](results/plots/kruskal_share.png)

The third plot shows the fraction of edges examined before the MST is complete. It differs from the number of edges sorted: Kruskal sorts the complete edge list, then may stop the DSU loop early for connected graphs. The benchmark generator ensures its undirected graphs are connected; disconnected inputs still use the full edge list and return a forest.

### Interpretation of execution time

Observed median times generally rise with graph size and number of edges, though small cases fluctuate due to JVM warmup, JIT compilation, allocation and background activity. Machine-dependent times should not be treated as proof of complexity. Exact scan counts and actual comparator calls offer more reproducible comparisons. Priority-queue behavior and memory allocation also influence Prim and sorting-heavy Kruskal.

## 6. Tests and limitations

JUnit tests cover graph operations, DSU, MST agreement, topological order, cycle detection, DAG negative-weight paths and multiple sources, SCC examples, isolated vertices and a 100,000-vertex path. The benchmark independently verifies reference values on all ten generated graph configurations. The assignment additionally requests randomized brute-force SCC comparison and Bellman-Ford cross-checks; these should be expanded for full test coverage if time permits. Git feature-branch history and a `v1.0` release tag must be created in the student's own repository and cannot be established by a ZIP file alone.

## 7. AI-use acknowledgement

AI assistance was used for code review, metric instrumentation, benchmark and plot generation, and report drafting. The graph algorithm foundations were developed and tested by the student with guided explanations. All generated changes should be reviewed and understood before submission and defense.
