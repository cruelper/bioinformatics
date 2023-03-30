package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindFrequentWordsWithMismatchesAndReverseComplements2 extends Intro01.FindTheMostFrequentWordsWithMismatchesInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindFrequentWordsWithMismatchesAndReverseComplements2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindFrequentWordsWithMismatchesAndReverseComplements2() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int d = sc.nextInt();
        int k = sc.nextInt();
        Map<String, String> patternsToReversePatterns = new HashMap<>();
        Map<String, Integer> patternsToCount = new HashMap<>();

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

        IntStream.range(0, text.length() - d)
                .forEach(i -> {
                    String pattern = text.substring(i, i + d);
                    patternsToReversePatterns.put(pattern, reverse.apply(pattern));
                    patternsToCount.put(pattern, 0);
                });

        patternsToReversePatterns.keySet()
                .forEach(pattern -> {
                    int count = patternsToReversePatterns.keySet().stream()
                            .parallel()
                            .mapToInt(p -> {
                                int count1 = (int) IntStream.range(0, pattern.length())
                                        .parallel()
                                        .filter(ii -> pattern.charAt(ii) != p.charAt(ii))
                                        .count();
                                int count2 = (int) IntStream.range(0, pattern.length())
                                        .parallel()
                                        .filter(ii -> patternsToReversePatterns.get(pattern).charAt(ii) != p.charAt(ii))
                                        .count();

//                                System.out.println(pattern + " " + (count1 == k ? 1:0) + "|" + reversedPattern + " " + (count2 == k ? 1:0));

                                return (count1 == k ? 1 : 0) + (count2 == k ? 1 : 0);
                            })
                            .reduce(0, Integer::sum);
                    patternsToCount.put(pattern, patternsToCount.get(pattern) + count);
                });

        Integer maxCount = Collections.max(patternsToCount.values());

        PrintWriter pw = new PrintWriter(outputFileName);
        patternsToCount.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), maxCount))
                .forEach(entry -> pw.println(entry.getKey() + " " + patternsToReversePatterns.get(entry.getKey())));
        pw.flush();
    }
}
