package Intro02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImplementGibbsSampler {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ImplementGibbsSampler();
//
//            List<List<Double>> matrix = GenerateProfileMatWithPseudocounts(
//                    List.of("GTCTATTTCAACACA", "GTCTATTTCAACACA", "GACTTCGGTGACCCT", "CTCTACTGTGATACT"),
//                    15, 0
//            );
//
//            System.out.println(
//                    FindPortableKmer("CAGAATATTGCCAGTTTGTCGATAGGTTTCGAGCTAGACGTGATTCCCCGGGTTGAGATCTACGCTCGTGCCATTCCATACCCAACCGTAAAGTATTCCGCACGATACTCGAAGACGGCCTATCTTTCTCACAGTTTCAATAGCACCATCGAGCTCCCCCTTTTAACACGATCCGGCATCGATCTGGCCTTACGGAGTGACTCCTCAAAGATGAGATATGCGAGGTTCGCGGCACAACGTGTCTATTTCAACACACGTGGAGAAGATTATGGCAAGTTCTAACCTACGAACCGCGACAAGTCAGAATATTGCCAGT", 5, matrix)
//            );
//
//            System.out.println(
//                    score(
//                            List.of("GTCTATTTCAACACA", "GTCTATTTCAACACA", "GACTTCGGTGACCCT", "CTCTACTGTGATACT"),
//                            matrix
//                    )
//            );
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static int score(List<String> motifs, List<List<Double>> profile) {
        String consensus = profile.stream().map(acgt ->
                String.valueOf("ACGT".charAt(
                        IntStream.range(0, 4).boxed().max(
                                Comparator.comparing(acgt::get)
                        ).orElse(-1)
                ))
        ).collect(Collectors.joining());

        long score = motifs.stream().map(motif ->
            IntStream.range(0, motif.length())
                .filter(ii -> motif.charAt(ii) != consensus.charAt(ii))
                .count()
        ).reduce(Long::sum).get();

        return (int)score;
    }

    public static void ImplementGibbsSampler() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        int k = sc.nextInt();
        int t = sc.nextInt();
        int N = sc.nextInt();
        String[] dna = new String[t];
        IntStream.range(0, t).forEach(i ->
                dna[i] = sc.next()
        );

        List<List<String>> res = new ArrayList<>();
        List<Integer> resScore = new ArrayList<>();
        Random random = new Random();

        IntStream.range(0, 500).parallel().forEach(i -> {
            List<String> bestMotifs = Arrays.stream(dna).map(str -> {
                int index = random.nextInt(dna[0].length() - k + 1);
                return str.substring(index, index + k);
            }).collect(Collectors.toList());

            List<List<Double>> profile = new ArrayList<>();

            List<String> motifs = new ArrayList<>(bestMotifs);

            for (int ii = 0; ii < N; ii++) {
                int j = random.nextInt(dna.length - 1);
                profile = GenerateProfileMatWithPseudocounts(motifs, k, j);
                String newMotif = FindPortableKmer(dna[j], k, profile);
                motifs.set(j, newMotif);
                double scoreNewMotifs = score(motifs, profile);
                double scoreOldMotifs = score(bestMotifs, profile);
                if (scoreNewMotifs < scoreOldMotifs) bestMotifs = motifs;
            }
            res.add(bestMotifs);
            resScore.add(score(bestMotifs, profile));
        });

        PrintWriter pw = new PrintWriter(outputFileName);
        int indexOfMinScore = resScore.indexOf(Collections.min(resScore));
        res.get(indexOfMinScore).forEach(pw::println);
        pw.flush();
    }

    public static List<List<Double>> GenerateProfileMatWithPseudocounts(List<String> motifs, int k, int ignored) {
        double motifNum = motifs.size();
        List<Character> patternComponets = List.of('A', 'C', 'G', 'T');
        List<List<Double>> matrixTranspon = new ArrayList<>();
        double[][] matrix = new double[4][k];
        int[] counts = {1, 1, 1, 1};

        for (int i = 0; i < k; i++) {
            double allcnts = 4;
            for (int j = 0; j < motifNum; j++) {
                if (j == ignored) continue;
                int index = patternComponets.indexOf(motifs.get(j).charAt(i));
                counts[index]++;
                allcnts++;
            }
            for (int j = 0; j < 4; j++) {
                matrix[j][i] = ((double) counts[j])/allcnts;
                counts[j] = 1;
            }
        }

        for (int i = 0; i < k; i++) {
            List<Double> col = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                col.add(matrix[j][i]);
            }
            matrixTranspon.add(col);
        }
        return matrixTranspon;
    }
}
