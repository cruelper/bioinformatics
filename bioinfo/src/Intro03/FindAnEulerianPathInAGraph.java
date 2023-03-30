package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FindAnEulerianPathInAGraph {
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
        Set<Integer> vertexesSet = new HashSet<>();
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
                vertexesSet.add(vertex);
                vertexesSet.addAll(neighbors);
            }
        }

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
        pw.print(
                cyclePrime.toString()
                        .replaceAll(", ", "->")
                        .substring(1, cyclePrime.toString().length() - 1)
        );
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
