package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class ReconstructAStringFromItsPairedComposition {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ReconstructAStringFromItsPairedComposition();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReconstructAStringFromItsPairedComposition() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        SortedMap<String, List<String>> graph = new TreeMap<>();
        Set<String> vertexSet = new HashSet<>();

        int k = sc.nextInt();
        int d = sc.nextInt();
        while (sc.hasNext()) {
            String text = sc.next();
            int dividerIndex = text.indexOf('|');
            String left = text.substring(0, dividerIndex);
            String right = text.substring(dividerIndex + 1);

            String leftPrefix = left.substring(0, left.length() - 1);
            String leftSuffix = left.substring(1);
            String rightPrefix = right.substring(0, right.length() - 1);
            String rightSuffix = right.substring(1);

            String prefix = leftPrefix + '|' + rightPrefix;
            String suffix = leftSuffix + '|' + rightSuffix;
            if (graph.containsKey(prefix)) graph.get(prefix).add(suffix);
            else  graph.put(prefix, new ArrayList<>(List.of(suffix)));

            vertexSet.add(prefix); vertexSet.add(suffix);
        }

        Map<String, Integer> vertexToCountInMinusOut = new HashMap<>();
        vertexSet.forEach(vertex -> vertexToCountInMinusOut.put(vertex, 0));
        graph.keySet().forEach(vertex -> vertexToCountInMinusOut.put(
                vertex, vertexToCountInMinusOut.get(vertex) + graph.get(vertex).size()
        ));
        graph.values().stream().flatMap(Collection::stream).forEach(vertex -> vertexToCountInMinusOut.put(
                vertex, vertexToCountInMinusOut.get(vertex) - 1
        ));

        String startVertex = vertexToCountInMinusOut.keySet().stream().filter(
                vertex -> vertexToCountInMinusOut.get(vertex) == 1
        ).findFirst().get();

        List<String> cyclePrime = traverseCycle(startVertex, graph);

        PrintWriter pw = new PrintWriter(outputFileName);
        Collections.reverse(cyclePrime);
        pw.print(cyclePrime.get(0).substring(0, k - 1));
        int cycleSize = cyclePrime.size();
        IntStream.range(1, cycleSize).forEach(i -> pw.print(cyclePrime.get(i).charAt(k - 2)));
        IntStream.range(cycleSize - k - d, cycleSize).forEach(i -> pw.print(cyclePrime.get(i).charAt(2 * k - 2)));
        pw.flush();
    }

    private static List<String> traverseCycle(String startVertex, Map<String, List<String>> graph) {
        List<String> cyclePrime = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(startVertex);
        while (!stack.isEmpty()) {
            String currentVertex = stack.pop();
            List<String> neighbors = graph.get(currentVertex);
            if (neighbors != null && !neighbors.isEmpty()) {
                String nextVertex = neighbors.remove(0);
                stack.push(currentVertex);
                stack.push(nextVertex);
            } else {
                cyclePrime.add(currentVertex);
            }
        }
        return cyclePrime;
    }
}
