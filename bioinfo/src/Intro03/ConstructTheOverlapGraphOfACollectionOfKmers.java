package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ConstructTheOverlapGraphOfACollectionOfKmers {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ConstructTheOverlapGraphOfACollectionOfKmers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void ConstructTheOverlapGraphOfACollectionOfKmers() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        SortedMap<String, SortedSet<String>> suffixes = new TreeMap<>();
        SortedMap<String, SortedSet<String>> prefixes = new TreeMap<>();

        while (sc.hasNext()) {
            String text = sc.next();
            String prefix = text.substring(0, text.length() - 1);
            String suffix = text.substring(1);

            if (prefixes.containsKey(prefix)) prefixes.get(prefix).add(text);
            else  prefixes.put(prefix, new TreeSet<>(Set.of(text)));

            if (suffixes.containsKey(suffix)) suffixes.get(suffix).add(text);
            else  suffixes.put(suffix, new TreeSet<>(Set.of(text)));
        }

        PrintWriter pw = new PrintWriter(outputFileName);

        SortedSet<String> leftTexts = suffixes.keySet().stream()
                .filter(prefixes::containsKey)
                .map(suffixes::get)
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
        leftTexts.forEach(leftText -> {
            prefixes.get(leftText.substring(1)).forEach(rightText ->
                    pw.println(leftText + " -> " + rightText)
            );
        });

        pw.flush();
    }
}
