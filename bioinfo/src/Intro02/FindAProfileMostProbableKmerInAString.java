package Intro02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAProfileMostProbableKmerInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAProfileMostProbableKmerInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void FindAProfileMostProbableKmerInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        String dna = sc.next();
        int k = sc.nextInt();
        double[][] profile = new double[4][k];
        IntStream.range(0, 4 * k).forEach(i ->
                profile[i / k][i % k] = sc.nextDouble()
        );

        Function<String, Double> probable = pattern -> IntStream.range(0, k).mapToDouble(i -> {
            switch (pattern.charAt(i)) {
                case 'A': return profile[0][i];
                case 'C': return profile[1][i];
                case 'G': return profile[2][i];
                case 'T': return profile[3][i];
            }
            return -1;
        }).reduce(1, (a,b) -> a * b);

        Map<String, Double> patternToProbable = IntStream.range(0, dna.length() - k + 1)
                .mapToObj(i -> dna.substring(i, i + k))
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toMap(e -> e, probable));

        Optional<Map.Entry<String, Double>> most = patternToProbable.entrySet().stream().max(Map.Entry.comparingByValue());

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(most.get().getKey());
        pw.flush();
    }
}
