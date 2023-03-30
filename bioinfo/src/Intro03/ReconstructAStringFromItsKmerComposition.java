package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReconstructAStringFromItsKmerComposition {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ReconstructAStringFromItsKmerComposition();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReconstructAStringFromItsKmerComposition() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        SortedMap<String, SortedSet<String>> suffixes = new TreeMap<>();
        SortedMap<String, SortedSet<String>> prefixes = new TreeMap<>();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Set<Integer> vertexesSet = new HashSet<>();
        Map<String, Integer> textToId = new HashMap<>();
        AtomicReference<Integer> idCounter = new AtomicReference<>(0);

        int k = sc.nextInt();
        while (sc.hasNext()) {
            String text = sc.next();
            String prefix = text.substring(0, text.length() - 1);
            String suffix = text.substring(1);

            if (prefixes.containsKey(prefix)) prefixes.get(prefix).add(text);
            else  prefixes.put(prefix, new TreeSet<>(Set.of(text)));

            if (suffixes.containsKey(suffix)) suffixes.get(suffix).add(text);
            else  suffixes.put(suffix, new TreeSet<>(Set.of(text)));
        }

        SortedSet<String> leftTexts = suffixes.keySet().stream()
                .filter(prefixes::containsKey)
                .map(suffixes::get)
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
        leftTexts.forEach(leftText -> {
            prefixes.get(leftText.substring(1)).forEach(rightText -> {
                int leftTextId = textToId.containsKey(leftText) ? textToId.get(leftText) : idCounter.getAndSet(idCounter.get() + 1);
                int rightTextId = textToId.containsKey(rightText) ? textToId.get(rightText) : idCounter.getAndSet(idCounter.get() + 1);
                if (!textToId.containsKey(leftText)) textToId.put(leftText, leftTextId);
                if (!textToId.containsKey(rightText)) textToId.put(rightText, rightTextId);
                if (graph.containsKey(leftTextId)) graph.get(leftTextId).add(rightTextId);
                else graph.put(leftTextId, new ArrayList<>(List.of(rightTextId)));
                vertexesSet.add(leftTextId);
                vertexesSet.add(rightTextId);
            });
        });

        Map<Integer, Integer> vertexToCountInMinusOut = new HashMap<>();
        vertexesSet.forEach(vertex -> vertexToCountInMinusOut.put(vertex, 0));
        graph.keySet().forEach(vertex -> vertexToCountInMinusOut.put(
                vertex, vertexToCountInMinusOut.get(vertex) + graph.get(vertex).size()
        ));
        graph.values().stream().flatMap(Collection::stream).forEach(vertex -> vertexToCountInMinusOut.put(
                vertex, vertexToCountInMinusOut.get(vertex) - 1
        ));

        Integer startVertex = vertexToCountInMinusOut.keySet().stream().filter(
                vertex -> vertexToCountInMinusOut.get(vertex) == 1
        ).findFirst().get();
        List<Integer> cyclePrime = traverseCycle(startVertex, graph);

        PrintWriter pw = new PrintWriter(outputFileName);
        Collections.reverse(cyclePrime);
        Map<Integer, String> idToText = textToId.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        pw.print(idToText.get(cyclePrime.get(0)));
        IntStream.range(1, cyclePrime.size()).forEach(i -> pw.print(idToText.get(cyclePrime.get(i)).charAt(k - 1)));
        pw.flush();
    }

    private static List<Integer> traverseCycle(Integer startVertex, Map<Integer, List<Integer>> graph) {
        List<Integer> cyclePrime = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(startVertex);
        while (!stack.isEmpty()) {
            Integer currentVertex = stack.pop();
            List<Integer> neighbors = graph.get(currentVertex);
            if (neighbors != null && !neighbors.isEmpty()) {
                Integer nextVertex = neighbors.remove(0);
                stack.push(currentVertex);
                stack.push(nextVertex);
            } else {
                cyclePrime.add(currentVertex);
            }
        }
        return cyclePrime;
    }
}
