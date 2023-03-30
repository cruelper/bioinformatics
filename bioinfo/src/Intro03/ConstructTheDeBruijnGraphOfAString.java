package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConstructTheDeBruijnGraphOfAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ConstructTheDeBruijnGraphOfAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ConstructTheDeBruijnGraphOfAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        SortedMap<String, SortedSet<String>> suffixes = new TreeMap<>();
        SortedMap<String, SortedSet<String>> prefixes = new TreeMap<>();

        int  k = sc.nextInt();
        String text = sc.next();

        IntStream.range(0, text.length() - k + 2).forEach(i -> {
            String kmer = text.substring(i, i + k - 1);
            String prefix = kmer.substring(0, kmer.length() - 1);
            String suffix = kmer.substring(1);

            if (prefixes.containsKey(prefix)) prefixes.get(prefix).add(kmer);
            else  prefixes.put(prefix, new TreeSet<>(Set.of(kmer)));

            if (suffixes.containsKey(suffix)) suffixes.get(suffix).add(kmer);
            else  suffixes.put(suffix, new TreeSet<>(Set.of(kmer)));
        });

        PrintWriter pw = new PrintWriter(outputFileName);

        SortedSet<String> leftTexts = suffixes.keySet().stream()
                .filter(prefixes::containsKey)
                .map(suffixes::get)
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
        leftTexts.forEach(leftText -> {
            pw.print(leftText + " -> ");
            SortedSet<String> set = prefixes.get(leftText.substring(1));
            int count = set.size();
            int counter = 0;
            for (String rightText : set) {
                pw.print(rightText);
                if (++counter != count) pw.print(",");
            }
            pw.println();
        });

        pw.flush();
    }
}
