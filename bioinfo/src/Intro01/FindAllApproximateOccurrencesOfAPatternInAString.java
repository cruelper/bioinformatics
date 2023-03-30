package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAllApproximateOccurrencesOfAPatternInAString extends Intro01.ComputeTheHammingDistanceBetweenTwoStrings {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAllApproximateOccurrencesOfAPatternInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAllApproximateOccurrencesOfAPatternInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String pattern = sc.nextLine();
        String text = sc.nextLine();
        int k = sc.nextInt();

        List<Integer> indices = IntStream.range(0, text.length() - pattern.length())
                .parallel()
                .filter(i -> {
                    int count = (int) IntStream.range(0, pattern.length())
                            .parallel()
                            .filter(ii -> {
                                return pattern.charAt(ii) != text.charAt(i + ii);
                            })
                            .count();
                    return count <= k;
                })
                .boxed()
                .collect(Collectors.toList());

        PrintWriter pw = new PrintWriter(outputFileName);
        Collections.sort(indices);
        indices.forEach(i -> pw.print(i + " "));
        pw.flush();
    }
}
