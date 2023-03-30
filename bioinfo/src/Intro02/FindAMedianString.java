package Intro02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAMedianString extends Intro02.ImplementMotifEnumeration {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAMedianString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAMedianString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        int k = sc.nextInt();
        sc.nextLine();
        List<String> dna = new ArrayList<>();
        while (sc.hasNext()) dna.add(sc.nextLine());

        BiFunction<String, String, Integer> isDNeighbor = (pattern1, pattern2) -> {
            return (int) IntStream.range(0, pattern1.length())
                    .parallel()
                    .filter(ii -> pattern1.charAt(ii) != pattern2.charAt(ii))
                    .count();
        };

        Map<String, Integer> neighborsToCount = IntStream.range(0, (int) Math.pow(4, k))
                .mapToObj(i -> {
                    StringBuilder numIn4RadixStrBuilder = new StringBuilder(Integer.toString(i, 4));
                    int leadingZerosCount = k - numIn4RadixStrBuilder.length();
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

        List<Set<String>> patterns = new ArrayList<>();
        dna.forEach(curDna -> {
            Set<String> patternsInDna = IntStream.range(0, curDna.length() - k + 1)
                    .mapToObj(i -> curDna.substring(i, i + k))
                    .collect(Collectors.toSet());
            patterns.add(patternsInDna);
        });

        neighborsToCount.keySet()
                .forEach(neighbor -> {
                    patterns.forEach(line -> {
                        OptionalInt d = line.stream().mapToInt(p -> {
                            return isDNeighbor.apply(p, neighbor);
                        }).min();
                        neighborsToCount.put(neighbor, neighborsToCount.get(neighbor) + d.getAsInt());
                    });
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        int minD = neighborsToCount.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .get().getValue();

        pw.println(
                neighborsToCount.entrySet()
                        .stream()
                        .filter(e -> e.getValue() == minD)
                        .findFirst()
                        .get().getKey()
        );
        pw.flush();
    }
}
