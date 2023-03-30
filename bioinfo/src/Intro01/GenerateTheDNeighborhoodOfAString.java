package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class GenerateTheDNeighborhoodOfAString extends Intro01.GenerateTheFrequencyArrayOfAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateTheDNeighborhoodOfAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateTheDNeighborhoodOfAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int k = sc.nextInt();

        List<String> neighbors = new ArrayList<>();

        BiFunction<String, String, Boolean> isDNeighbor = (pattern1, pattern2) -> {
            return k >= (int) IntStream.range(0, pattern1.length())
                    .parallel()
                    .filter(ii -> pattern1.charAt(ii) != pattern2.charAt(ii))
                    .count();
        };

        IntStream.range(0, (int) Math.pow(4, text.length()))
                .forEach(i -> {
                    StringBuilder numIn4RadixStrBuilder = new StringBuilder(Integer.toString(i, 4));
                    int leadingZerosCount = text.length() - numIn4RadixStrBuilder.length();
                    IntStream.range(0, leadingZerosCount)
                            .forEach(j -> numIn4RadixStrBuilder.insert(0, 0));

                    // A=0, C=1, G=2, T=3
                    String numIn4RadixStr = numIn4RadixStrBuilder.toString()
                            .replace('0', 'A')
                            .replace('1', 'C')
                            .replace('2', 'G')
                            .replace('3', 'T');

                    if (isDNeighbor.apply(numIn4RadixStr, text)) neighbors.add(numIn4RadixStr);
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        neighbors.forEach(pw::println);
        pw.flush();
    }
}
