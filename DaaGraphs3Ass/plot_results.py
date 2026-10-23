import csv, math
from pathlib import Path
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
p=Path('results'); rows=list(csv.DictReader((p/'results.csv').open()))
for r in rows:
    for k in ('n','m','dsu_ops','edge_scans','comparisons','max_depth','result'):r[k]=int(r[k])
    r['time_ms']=float(r['time_ms'])
(p/'plots').mkdir(exist_ok=True)
algos=['Kruskal','Prim','Kosaraju','Kahn','DagShortestPath']
plt.figure(figsize=(10,6))
for a in algos:
 for d in ('sparse','dense'):
  rs=sorted((r for r in rows if r['algorithm']==a and r['graph']==d),key=lambda x:x['n'])
  ys=[r['dsu_ops']+r['edge_scans']+r['comparisons'] for r in rs]
  plt.plot([r['n'] for r in rs],ys,marker='o',label=f'{a} {d}')
plt.xscale('log');plt.yscale('log');plt.xlabel('Vertices (n)');plt.ylabel('Counted operations (DSU + scans + comparisons)');plt.title('Counted operations vs graph size');plt.legend(fontsize=8,ncol=2);plt.grid(alpha=.3);plt.tight_layout();plt.savefig(p/'plots/operations.png',dpi=170);plt.close()
plt.figure(figsize=(10,6))
for a in algos:
 for d in ('sparse','dense'):
  rs=sorted((r for r in rows if r['algorithm']==a and r['graph']==d),key=lambda x:x['n'])
  plt.plot([r['n'] for r in rs],[r['edge_scans']/r['m'] for r in rs],marker='o',label=f'{a} {d}')
plt.xscale('log');plt.xlabel('Vertices (n)');plt.ylabel('edgeScans / m');plt.title('Edge scans per input edge');plt.legend(fontsize=8,ncol=2);plt.grid(alpha=.3);plt.tight_layout();plt.savefig(p/'plots/scans_per_edge.png',dpi=170);plt.close()
plt.figure(figsize=(9,5))
for d in ('sparse','dense'):
 rs=sorted((r for r in rows if r['algorithm']=='Kruskal' and r['graph']==d),key=lambda x:x['n'])
 plt.plot([r['n'] for r in rs],[r['edge_scans']/r['m'] for r in rs],marker='o',label=d)
plt.xscale('log');plt.ylim(0,1.1);plt.xlabel('Vertices (n)');plt.ylabel('Kruskal edgeScans / m');plt.title('Share of edges examined by Kruskal');plt.legend();plt.grid(alpha=.3);plt.tight_layout();plt.savefig(p/'plots/kruskal_share.png',dpi=170);plt.close()
print('Generated three plots')
