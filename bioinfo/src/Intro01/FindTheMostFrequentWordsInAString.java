package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FindTheMostFrequentWordsInAString extends Intro01.ComputeTheNumberOfTimesAPatternAppearsInAText {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindTheMostFrequentWordsInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindTheMostFrequentWordsInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int k = sc.nextInt();
        Map<String, Integer> patternToCount = new Hashtable<>();

        long start = System.currentTimeMillis();
        IntStream.range(0, text.length() - k + 1)
                .forEach(index -> {
                    String pattern = text.substring(index, index + k);
                    patternToCount.put(pattern, patternToCount.containsKey(pattern) ? patternToCount.get(pattern) + 1 : 1);
                });

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Прошло времени, мс: " + elapsed);

        int maxCount = Collections.max(patternToCount.entrySet(), Map.Entry.comparingByValue()).getValue();
        PrintWriter pw = new PrintWriter(outputFileName);
        patternToCount.forEach((pattern, count) -> {
            if (count == maxCount) pw.println(pattern);
        });
        pw.flush();
    }
}
