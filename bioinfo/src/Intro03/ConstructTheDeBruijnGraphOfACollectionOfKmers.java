package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConstructTheDeBruijnGraphOfACollectionOfKmers {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ConstructTheDeBruijnGraphOfACollectionOfKmers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ConstructTheDeBruijnGraphOfACollectionOfKmers() throws FileNotFoundException {
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

        PrintWriter pw = new PrintWriter(outputFileName);
        graph.keySet().forEach(key -> {
            List<String> values = graph.get(key);
            Collections.sort(values);
            pw.print(key + " -> ");
            IntStream.range(0, values.size()).forEach(i -> {
                pw.print(values.get(i));
                if (i < values.size() - 1) pw.print(",");
            });
            pw.println();
        });
        pw.flush();
    }
}
