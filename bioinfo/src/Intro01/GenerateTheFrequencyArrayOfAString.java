package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class GenerateTheFrequencyArrayOfAString extends Intro01.FindFrequentWordsWithMismatchesAndReverseComplements {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            GenerateTheFrequencyArrayOfAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateTheFrequencyArrayOfAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int k = sc.nextInt();

        Map<String, Integer> patternToCount = new HashMap<>();
        IntStream.range(0, text.length() - k + 1)
                .forEach(i -> {
                    String pattern = text.substring(i, i + k);
                    if (!patternToCount.containsKey(pattern)) patternToCount.put(pattern, 1);
                    else patternToCount.put(pattern, patternToCount.get(pattern) + 1);
                });

        List<Integer> counts = new ArrayList<>();

        IntStream.range(0, (int) Math.pow(4, k))
                .forEach(i -> {
                    StringBuilder numIn4RadixStrBuilder = new StringBuilder(Integer.toString(i, 4));
                    int leadingZerosCount = k - numIn4RadixStrBuilder.length();
                    IntStream.range(0, leadingZerosCount)
                            .forEach(j -> numIn4RadixStrBuilder.insert(0, 0));

                    // A=0, C=1, G=2, T=3
                    String numIn4RadixStr = numIn4RadixStrBuilder.toString()
                            .replace('0', 'A')
                            .replace('1', 'C')
                            .replace('2', 'G')
                            .replace('3', 'T');

                    counts.add(patternToCount.getOrDefault(numIn4RadixStr, 0));
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        counts.forEach(count -> pw.print(count + " "));
        pw.flush();
    }
}
