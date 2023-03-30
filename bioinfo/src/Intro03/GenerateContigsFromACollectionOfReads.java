package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateContigsFromACollectionOfReads {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateContigsFromACollectionOfReads();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateContigsFromACollectionOfReads() throws FileNotFoundException {
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

        List<Map.Entry<String, String>> edges = new ArrayList<>();
        graph.keySet().forEach(key -> graph.get(key).forEach(value -> edges.add(new AbstractMap.SimpleEntry<>(key, value))));

        int curVertex = 0;
        List<String> contig = new ArrayList<>();
        List<List<String>> contigs = new ArrayList<>();
        while (!edges.isEmpty()) {
            Map.Entry<String, String> edge = edges.get(curVertex);
            if (contig.size() == 0) contig.add(edge.getKey());
            contig.add(edge.getValue());
            edges.remove(curVertex);

            List<Integer> newCurVertices = new ArrayList<>();
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).getKey().equals(edge.getValue())) {
                    newCurVertices.add(i);
                }
            }
            List<Integer> candidates = newCurVertices.stream().filter(vetex -> {
                String startVertex = edges.get(vetex).getKey();
                int outs = graph.get(startVertex).size();
                long ins = graph.values().stream().flatMap(Collection::stream).filter(value -> value.equals(startVertex)).count();
                return ins == 1 && outs == 1;
            }).collect(Collectors.toList());

            if (candidates.isEmpty()) {
//                int newVert = newCurVertices.get(0);
//                contig.add(edges.get(newVert).getValue());
//                edges.remove(newVert);
                contigs.add(contig);
                contig = new ArrayList<>();

                List<String> values = edges.stream().map(Map.Entry::getValue).collect(Collectors.toList());
                curVertex = IntStream.range(0, edges.size()).filter(i -> !values.contains(edges.get(i).getKey())).findFirst().orElse(0);
            } else curVertex = candidates.get(0);
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
}
