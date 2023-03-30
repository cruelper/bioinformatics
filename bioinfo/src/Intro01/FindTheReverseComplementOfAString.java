package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FindTheReverseComplementOfAString extends Intro01.FindTheMostFrequentWordsInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindTheReverseComplementOfAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindTheReverseComplementOfAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();

        String reversedText = new StringBuilder(text)
                .reverse()
                .chars()
                .parallel()
                .mapToObj(c -> {
                    switch (c) {
                        case 'A':
                            return "T";
                        case 'T':
                            return "A";
                        case 'C':
                            return "G";
                        case 'G':
                            return "C";
                    }
                    return "";
                })
                .collect(Collectors.joining());

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(reversedText);
        pw.flush();
    }
}
