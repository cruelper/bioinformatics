package Intro02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImplementGreedyMotifSearch {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ImplementGreedyMotifSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImplementGreedyMotifSearch() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        int k = sc.nextInt();
        int t = sc.nextInt();
        String[] dna = new String[t];
        IntStream.range(0, t).forEach(i ->
                dna[i] = sc.next()
        );

        AtomicReference<List<String>> bestMotifs = new AtomicReference<>(
                Arrays.stream(dna).map(str -> str.substring(0, k)).collect(Collectors.toList())
        );
        String seq = dna[0];

        IntStream.range(0, seq.length() - k + 1).forEach(i -> {
            String kmers = seq.substring(i, i + k);
            List<String> motifs = new ArrayList<>(List.of(kmers));
            IntStream.range(1, dna.length).forEach(j -> {
                        List<List<Double>> profile = GenerateProfileMat(motifs);
                        String newMotif = FindPortableKmer(dna[j], k, profile);
                        motifs.add(newMotif);
                    }
            );
            double scoreNewMotifs = score(motifs);
            double scoreOldMotifs = score(bestMotifs.get());
            if (scoreNewMotifs < scoreOldMotifs) bestMotifs.set(motifs);
        });

        PrintWriter pw = new PrintWriter(outputFileName);
        bestMotifs.get().forEach(pw::println);
        pw.flush();
    }

    static List<List<Double>> GenerateProfileMat(List<String> motifs) {
        double motifNum = motifs.size();
        List<Character> patternComponets = List.of('A', 'C', 'G', 'T');
        List<List<Double>> matrixTranspon = new ArrayList<>();
        IntStream.range(0, motifs.get(0).length()).forEach(i -> {
            List<Character> colComponents = motifs.stream().map(motif -> motif.charAt(i)).collect(Collectors.toList());
            List<Double> colProfile = patternComponets.stream().map(c -> {
                return ((double)Collections.frequency(colComponents, c)) / motifNum;
            }).collect(Collectors.toList());
            matrixTranspon.add(colProfile);
        });
        return matrixTranspon;
    }

    public static String FindPortableKmer(String text, int k, List<List<Double>> matrix) {
        List<Double> prList = new ArrayList<>();
        IntStream.range(0, text.length() - k + 1).forEach(i -> {
            String pattern = text.substring(i, i + k);
            double KmerPortable = IntStream.range(0, k).mapToDouble(j -> {
                switch (pattern.charAt(j)) {
                    case 'A': return matrix.get(j).get(0);
                    case 'C': return matrix.get(j).get(1);
                    case 'G': return matrix.get(j).get(2);
                    case 'T': return matrix.get(j).get(3);
                }
                return -1;
            }).reduce(1, (a,b) -> a * b);
            prList.add(KmerPortable);

        });
        int i = prList.indexOf(Collections.max(prList));
        String maxKmer = text.substring(i, i + k);
        return maxKmer;
    }

    public static int score(List<String> motifs) {
        long score = IntStream.range(0, motifs.get(0).length()).mapToObj(i -> {
            String motif = motifs.stream()
                    .map(s -> Character.toString(s.charAt(i)))
                    .collect(Collectors.joining());
            return "ACGT".chars().mapToLong(c -> {
                String text = String.join("", Collections.nCopies(motif.length(), Character.toString(c)));
                return IntStream.range(0, motif.length())
                        .filter(ii -> motif.charAt(ii) != text.charAt(ii))
                        .count();
            }).min().getAsLong();
        }).reduce(Long::sum).get();
        return (int)score;
    }
}
