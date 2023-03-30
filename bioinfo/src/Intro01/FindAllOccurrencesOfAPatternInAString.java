package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAllOccurrencesOfAPatternInAString extends Intro01.FindTheReverseComplementOfAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAllOccurrencesOfAPatternInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAllOccurrencesOfAPatternInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String pattern = sc.nextLine();
        String text = sc.nextLine();
        int patternSize = pattern.length();

        List<Integer> indices = IntStream.range(0, text.length())
                .parallel()
                .filter(index -> {
                    if (text.charAt(index) == pattern.charAt(0)) {
                        return text.substring(index, Math.min(index + patternSize, text.length())).equals(pattern);
                    }
                    return false;
                })
                .boxed()
                .collect(Collectors.toList());

        PrintWriter pw = new PrintWriter(outputFileName);
        indices.forEach(i -> pw.print(i + " "));
        pw.flush();
    }
}
