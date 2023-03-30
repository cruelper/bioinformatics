package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class GenerateContigsFromACollectionOfReads2 {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateContigsFromACollectionOfReads2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateContigsFromACollectionOfReads2() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        List<String> patterns = new ArrayList<>();
        while (sc.hasNext()) patterns.add(sc.next());

        SortedMap<String, List<String>> graph = new TreeMap<>();
        patterns.forEach(pattern -> {
            String prefix = pattern.substring(0, pattern.length() - 1);
            String suffix = pattern.substring(1);
            if (graph.containsKey(prefix)) graph.get(prefix).add(suffix);
            else graph.put(prefix, new ArrayList<>(List.of(suffix)));
        });

        List<List<String>> contigs = new ArrayList<>();
        Map.Entry<Map<String, Integer> , Map<String, Integer>> inToOutPaths = getInToOutPaths(graph);
        Map<String, Integer> in = inToOutPaths.getKey();
        Map<String, Integer> out = inToOutPaths.getValue();

        for (String node : out.keySet()) {
            int ins = in.get(node);
            int outs = out.get(node);
            if (outs > 0 && !(outs == 1 && ins == 1)) {
                contigs.addAll(findContigs(graph, node, in, out));
            }
        }

        PrintWriter pw = new PrintWriter(outputFileName);
        int k = contigs.get(0).get(0).length();
        contigs.forEach(curContig -> {
            pw.print(curContig.get(0));
            IntStream.range(1, curContig.size()).forEach(i -> pw.print(curContig.get(i).charAt(k - 1)));
            pw.print(" ");
        });
        pw.flush();
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
            String node,
            Map<String, Integer> indegree,
            Map<String, Integer> outdegree
    ) {
        List<List<String>> contigs = new ArrayList<>();
        for (String next : graph.get(node)) {
            List<String> newPath = new ArrayList<>(List.of(node, next));
            int ins = indegree.get(next);
            int outs = outdegree.get(next);
            while (ins == 1 && outs == 1) {
                node = next;
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
