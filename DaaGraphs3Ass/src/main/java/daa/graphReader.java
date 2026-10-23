package daa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class graphReader {

    public static Graph read(String filename)
        throws IOException{

            List<String> lines = Files.readAllLines
                    (
                            Path.of(filename)
                    );

            String[] header = lines.get(0).trim().split("\\s+");

            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);

            if(m < 0 || lines.size() != m+ 1){
                throw new IllegalArgumentException("Incorrect edges");
            }

            boolean directed = header[2].equals("directed");

            Graph graph = new Graph(n, directed);

            for(int i =  0; i< m; i++){
                String[] parts = lines.get(i + 1).trim().split("\\s+");

                System.out.println(
                        java.util.Arrays.toString(parts)
                );

                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int w = Integer.parseInt(parts[2]);

                graph.addEdge(u, v, w);
            }

            return graph;
    }
}
