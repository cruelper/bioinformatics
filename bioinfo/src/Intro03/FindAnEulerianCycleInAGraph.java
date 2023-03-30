package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class FindAnEulerianCycleInAGraph {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAnEulerianCycleInAGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAnEulerianCycleInAGraph() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        Map<Integer, List<Integer>> graph = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                String[] parts = line.split(" -> ");
                Integer vertex = Integer.parseInt(parts[0]);
                List<Integer> neighbors = new ArrayList<>();
                for (String neighbor : parts[1].split(",")) {
                    neighbors.add(Integer.parseInt(neighbor));
                }
                graph.put(vertex, neighbors);
            }
        }

        List<Integer> cycle = new ArrayList<>();
        Integer startVertex = (new Random()).nextInt(graph.size());
        cycle.add(startVertex);
        while (true) {
            Integer newStart = findNewStart(graph, cycle);
            if (newStart == null) break;
            List<Integer> cyclePrime = traverseCycle(newStart, graph);
            cycle = mergeCycles(cycle, cyclePrime);
        }

        PrintWriter pw = new PrintWriter(outputFileName);
        Collections.reverse(cycle);
        pw.print(
                cycle.toString()
                        .replaceAll(", ", "->")
                        .substring(1, cycle.toString().length() - 1)
        );
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
                graph.get(nextVertex).remove(currentVertex);
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
