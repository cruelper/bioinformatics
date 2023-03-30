package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindTheMostFrequentWordsWithMismatchesInAString extends Intro01.FindAllApproximateOccurrencesOfAPatternInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindTheMostFrequentWordsWithMismatchesInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindTheMostFrequentWordsWithMismatchesInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int d = sc.nextInt();
        int k = sc.nextInt();
        List<String> patterns = new ArrayList<>();

        IntStream.range(0, text.length() - d)
                .forEach(i -> {
                    patterns.add(text.substring(i, i + d));
                });

        List<Long> counts = patterns.stream()
                .mapToLong(pattern -> {
                    return patterns.stream()
                            .parallel()
                            .filter(p -> {
                                int count = (int) IntStream.range(0, pattern.length())
                                        .parallel()
                                        .filter(ii -> pattern.charAt(ii) != p.charAt(ii))
                                        .count();
                                return count == k;
                            })
                            .count();
                })
                .boxed()
                .collect(Collectors.toList());

        Long maxCount = Collections.max(counts);

        PrintWriter pw = new PrintWriter(outputFileName);
        IntStream.range(0, counts.size())
                .filter(i -> Objects.equals(counts.get(i), maxCount))
                .mapToObj(patterns::get)
                .collect(Collectors.toSet())
                .forEach(pw::println);
        pw.flush();
    }
}
