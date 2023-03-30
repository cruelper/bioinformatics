package Intro02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImplementDistanceBetweenPatternAndStrings {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ImplementDistanceBetweenPatternAndStrings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImplementDistanceBetweenPatternAndStrings() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        List<String> dnas = new ArrayList<>();
        String pattern = sc.next();
        while (sc.hasNext()) dnas.add(sc.next());

        long d = dnas.stream().mapToLong(dna -> {
            return IntStream.range(0, dna.length() - pattern.length()).mapToLong(i -> {
                return IntStream.range(0, pattern.length()).filter(
                        j -> pattern.charAt(j) != dna.charAt(i + j)
                ).count();
            }).min().orElse(-1);
        }).sum();

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.print(d);
        pw.flush();
    }
}
