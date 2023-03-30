package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAKUniversalCircularString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAKUniversalCircularString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAKUniversalCircularString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        SortedMap<String, SortedSet<String>> suffixes = new TreeMap<>();
        SortedMap<String, SortedSet<String>> prefixes = new TreeMap<>();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<String, Integer> textToId = new HashMap<>();
        AtomicReference<Integer> idCounter = new AtomicReference<>(0);

        int k = sc.nextInt(); k--;
        int curNum = (int)Math.pow(2, k) - 1;
        while (curNum >= 0) {
            StringBuilder numStrBuilder = new StringBuilder(Integer.toBinaryString(curNum));
            int leadingZerosCount = k - numStrBuilder.length();
            IntStream.range(0, leadingZerosCount)
                    .forEach(j -> numStrBuilder.insert(0, 0));
            String text = numStrBuilder.toString();

            String prefix = text.substring(0, text.length() - 1);
            String suffix = text.substring(1);

            if (prefixes.containsKey(prefix)) prefixes.get(prefix).add(text);
            else  prefixes.put(prefix, new TreeSet<>(Set.of(text)));

            if (suffixes.containsKey(suffix)) suffixes.get(suffix).add(text);
            else  suffixes.put(suffix, new TreeSet<>(Set.of(text)));

            curNum--;
        }

        SortedSet<String> leftTexts = suffixes.keySet().stream()
                .filter(prefixes::containsKey)
                .map(suffixes::get)
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
        leftTexts.forEach(leftText -> {
            prefixes.get(leftText.substring(1)).forEach(rightText -> {
                int leftTextId = textToId.containsKey(leftText) ? textToId.get(leftText) : idCounter.getAndSet(idCounter.get() + 1);
                if (!textToId.containsKey(leftText)) textToId.put(leftText, leftTextId);
                int rightTextId = textToId.containsKey(rightText) ? textToId.get(rightText) : idCounter.getAndSet(idCounter.get() + 1);
                if (!textToId.containsKey(rightText)) textToId.put(rightText, rightTextId);

                if (graph.containsKey(leftTextId)) graph.get(leftTextId).add(rightTextId);
                else graph.put(leftTextId, new ArrayList<>(List.of(rightTextId)));
            });
        });

        List<Integer> cycle = new ArrayList<>();
        Integer startVertex = 0;
        cycle.add(startVertex);
        while (true) {
            Integer newStart = findNewStart(graph, cycle);
            if (newStart == null) break;
            List<Integer> cyclePrime = traverseCycle(newStart, graph);
            cycle = mergeCycles(cycle, cyclePrime);
        }

        PrintWriter pw = new PrintWriter(outputFileName);
        Collections.reverse(cycle);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cycle.size() - 1; i++) {
            String binStr = Integer.toBinaryString(cycle.get(i));
            stringBuilder.append(binStr.charAt(binStr.length() - 1));
        }
        pw.print(stringBuilder.toString());
        pw.flush();
    }

    private static Integer findNewStart(Map<Integer, List<Integer>> graph, List<Integer> cycle) {
        Optional<Integer> start = cycle.stream()
                .filter(vertex -> graph.get(vertex) != null && !graph.get(vertex).isEmpty())
                .findFirst();
        return start.orElse(null);
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

    private static List<Integer> mergeCycles(List<Integer> cycle, List<Integer> cyclePrime) {
        Integer endVertex = cycle.get(cycle.size() - 1);
        int mergeIndex = cyclePrime.indexOf(endVertex);
        List<Integer> mergedCycle = new ArrayList<>();
        mergedCycle.addAll(cycle);
        mergedCycle.addAll(cyclePrime.subList(mergeIndex + 1, cyclePrime.size()));
        return mergedCycle;
    }
}
