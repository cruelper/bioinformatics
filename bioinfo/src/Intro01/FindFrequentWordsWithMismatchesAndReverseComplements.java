package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindFrequentWordsWithMismatchesAndReverseComplements extends Intro01.FindFrequentWordsWithMismatchesAndReverseComplements2 {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindFrequentWordsWithMismatchesAndReverseComplements();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindFrequentWordsWithMismatchesAndReverseComplements() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int d = sc.nextInt();
        int k = sc.nextInt();

        BiFunction<String, String, Boolean> isDNeighbor = (pattern1, pattern2) -> {
            return k >= (int) IntStream.range(0, pattern1.length())
                    .parallel()
                    .filter(ii -> pattern1.charAt(ii) != pattern2.charAt(ii))
                    .count();
        };

        final Function<String, String> reverse = pattern -> {
            return new StringBuilder(pattern)
                    .reverse()
                    .chars()
                    .parallel()
                    .mapToObj(c -> {
                        switch (c) {
                            case 'A':
                                return "T";
                            case 'T':
                                return "A";
                            case 'C':
                                return "G";
                            case 'G':
                                return "C";
                        }
                        return "";
                    })
                    .collect(Collectors.joining());
        };

        Map<String, Integer> patternToCount = IntStream.range(0, text.length() - d + 1)
                .mapToObj(i -> text.substring(i, i + d))
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum));

        Map<String, Integer> neighborsToCount = IntStream.range(0, (int) Math.pow(4, d))
                .mapToObj(i -> {
                    StringBuilder numIn4RadixStrBuilder = new StringBuilder(Integer.toString(i, 4));
                    int leadingZerosCount = d - numIn4RadixStrBuilder.length();
                    IntStream.range(0, leadingZerosCount)
                            .forEach(j -> numIn4RadixStrBuilder.insert(0, 0));

                    // A=0, C=1, G=2, T=3
                    return numIn4RadixStrBuilder.toString()
                            .replace('0', 'A')
                            .replace('1', 'C')
                            .replace('2', 'G')
                            .replace('3', 'T');
                })
                .collect(Collectors.toMap(e -> e, e -> 0));

        Map<String, String> neighborsToReversNeighbour = neighborsToCount.keySet()
                .stream()
                .collect(Collectors.toMap(e -> e, reverse));

        patternToCount.keySet()
                .forEach(pattern -> {
                    neighborsToCount.keySet()
                            .stream().parallel()
                            .forEach(neighbor -> {
                                if (isDNeighbor.apply(pattern, neighbor)) {
                                    int newCount = patternToCount.get(pattern) + neighborsToCount.get(neighbor);
                                    neighborsToCount.put(neighbor, newCount);
                                }
                                if (isDNeighbor.apply(pattern, neighborsToReversNeighbour.get(neighbor))) {
                                    int newCount = patternToCount.get(pattern) + neighborsToCount.get(neighbor);
                                    neighborsToCount.put(neighbor, newCount);
                                }
                            });
                });

        int maxNeighborCount = Collections.max(neighborsToCount.values());

        PrintWriter pw = new PrintWriter(outputFileName);
        neighborsToCount.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), maxNeighborCount))
                .forEach(entry -> pw.println(entry.getKey() + " "));
        pw.flush();
    }
}
