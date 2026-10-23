# DAA Assignment 3 — Graph Algorithms

Java Maven project with DSU, Kruskal, Prim, Kosaraju, Kahn and DAG shortest paths.

## Build and test

Use JDK 21 or later and Maven:

```bash
mvn clean test
mvn package
```

## Run benchmark and regenerate plots

From the project root:

```bash
java -Xmx5g -cp target/classes daa.BenchMark
python3 plot_results.py
```

Benchmark regenerates `results/results.csv` (50 data rows) and validates the exact assignment reference values. The Python plot script requires `matplotlib` (`sudo pacman -S python-matplotlib` on CachyOS).

## Layout

- `src/main/java/daa/`: algorithms, graph core, metrics and benchmark
- `src/test/java/daa/`: JUnit tests
- `data/`: example graph input files
- `results/results.csv`: verified five-run median benchmark output
- `results/plots/`: three PNG plots
- `REPORT.md`: analysis, complexity, counters, plots and discussion

## AI assistance

AI was used for reviewing code, adding metric instrumentation, creating benchmark output and plots, and drafting documentation. Review the code and report before submission.

## Git and Moodle

The ZIP is a source-code deliverable; it does not substitute for a GitHub repository with the required `main` branch and `v1.0` tag. Replace the placeholder filename with your own name, surname and group when submitting.

## Author
Name: Ziyakmetova Zhasmina
Group: SE-2509
