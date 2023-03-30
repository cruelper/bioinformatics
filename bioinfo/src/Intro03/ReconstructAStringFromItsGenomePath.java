package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReconstructAStringFromItsGenomePath {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ReconstructAStringFromItsGenomePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReconstructAStringFromItsGenomePath() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        List<String> dna = new ArrayList<>();
        while (sc.hasNext()) dna.add(sc.next());

        String totalText = dna.get(0) +
                IntStream.range(1, dna.size())
                        .mapToObj(i -> String.valueOf(
                                dna.get(i).charAt(dna.get(i).length() - 1)
                        )).collect(Collectors.joining());

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(totalText);
        pw.flush();
    }
}
