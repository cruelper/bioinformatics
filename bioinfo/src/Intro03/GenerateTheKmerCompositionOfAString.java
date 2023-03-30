package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class GenerateTheKmerCompositionOfAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateTheKmerCompositionOfAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void GenerateTheKmerCompositionOfAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        int k = sc.nextInt();
        String text = sc.next();

        SortedMap<String, Integer> kmerToCount = new TreeMap<>();
        IntStream.range(0, text.length() - k + 1)
                .forEach(i -> {
                    String kmer = text.substring(i, i + k);
                    boolean isContainsKey = kmerToCount.containsKey(kmer);
                    kmerToCount.put(kmer, isContainsKey ? kmerToCount.get(kmer) : 1);
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        kmerToCount.keySet().forEach(key -> {
            IntStream.range(0, kmerToCount.get(key)).forEach(i -> pw.println(key));
        });
        pw.flush();
    }
}
