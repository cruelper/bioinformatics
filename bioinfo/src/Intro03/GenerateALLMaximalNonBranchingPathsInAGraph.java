package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class GenerateALLMaximalNonBranchingPathsInAGraph {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateALLMaximalNonBranchingPathsInAGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateALLMaximalNonBranchingPathsInAGraph() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);

        SortedMap<String, List<String>> graph = new TreeMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                String[] parts = line.split(" -> ");
                String vertex = parts[0];
                List<String> neighbors = new ArrayList<>();
                neighbors.addAll(Arrays.asList(parts[1].split(",")));
                graph.put(vertex, neighbors);
            }
        }

        List<List<String>> contigs = new ArrayList<>();
        List<String> isolatedVertex = new ArrayList<>();
        Set<String> existAtContigs = new HashSet<>();
        Map.Entry<Map<String, Integer> , Map<String, Integer>> inToOutPaths = getInToOutPaths(graph);
        Map<String, Integer> in = inToOutPaths.getKey();
        Map<String, Integer> out = inToOutPaths.getValue();

        for (String node : out.keySet()) {
            int ins = in.get(node);
            int outs = out.get(node);
            if (!(outs == 1 && ins == 1)) {
                if (outs > 0) {
                    contigs.addAll(findContigs(graph, node, in, out, existAtContigs));
                }
            } else {
                isolatedVertex.add(node);
            }
        }
        isolatedVertex.removeAll(existAtContigs);
        contigs.addAll(processIsolatedVertex(graph, isolatedVertex));

        PrintWriter pw = new PrintWriter(outputFileName);
        int k = contigs.get(0).get(0).length();
        contigs.forEach(curContig -> {
            pw.print(curContig.get(0));
            pw.print(" -> ");
            IntStream.range(1, curContig.size() - 1).forEach(i ->
                    pw.print(curContig.get(i) + " -> ")
            );
            pw.print(curContig.get(curContig.size() - 1));
            pw.println();
        });
        pw.flush();
    }

    public static List<List<String>> processIsolatedVertex(Map<String, List<String>> graph, List<String> isolatedVertex) {
        List<List<String>> contigs = new ArrayList<>();
        List<String> processedVertex = new ArrayList<>();
        for (String node : isolatedVertex) {
            if (!processedVertex.contains(node)) {
                List<String> contig = new ArrayList<>();
                contig.add(node);
                String curVertex = graph.get(node).get(0);
                while (!curVertex.equals(contig.get(0))) {
                    processedVertex.add(curVertex);
                    contig.add(curVertex);
                    curVertex = graph.get(curVertex).get(0);
                }
                contig.add(curVertex);
                contigs.add(contig);
            }
        }
        return contigs;
    }

    public static Map.Entry<Map<String, Integer> , Map<String, Integer> > getInToOutPaths(Map<String, List<String>> graph) {
        Map<String, Integer> in = new HashMap<>();
        Map<String, Integer> out = new HashMap<>();
        for (String baseVertex : graph.keySet()) {
            if (!in.containsKey(baseVertex)) in.put(baseVertex, 0);
            out.put(baseVertex, graph.get(baseVertex).size());
            for (String targetVertex : graph.get(baseVertex)) {
                if (in.containsKey(targetVertex)) in.put(targetVertex, in.get(targetVertex) + 1);
                else in.put(targetVertex, 1);
                if (!out.containsKey(targetVertex)) out.put(targetVertex, 0);
            }
        }
        return new AbstractMap.SimpleEntry<>(in, out);
    }

    public static List<List<String>> findContigs(
            Map<String, List<String>> graph,
            String nodee,
            Map<String, Integer> indegree,
            Map<String, Integer> outdegree,
            Set<String> existAtContigs
    ) {
        List<List<String>> contigs = new ArrayList<>();
        for (String next : graph.get(nodee)) {
            String node = nodee;
            List<String> newPath = new ArrayList<>(List.of(node, next));
            int ins = indegree.get(next);
            int outs = outdegree.get(next);
            existAtContigs.add(node);
            while (ins == 1 && outs == 1) {
                node = next;
                existAtContigs.add(node);
                next = graph.get(node).get(0);
                newPath.add(next);
                ins = indegree.get(next);
                outs = outdegree.get(next);
            }
            contigs.add(newPath);
        }
        return contigs;
    }
}
