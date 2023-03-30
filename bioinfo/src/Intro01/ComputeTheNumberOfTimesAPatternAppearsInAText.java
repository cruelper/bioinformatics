package Intro01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ComputeTheNumberOfTimesAPatternAppearsInAText {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ComputeTheNumberOfTimesAPatternAppearsInAText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ComputeTheNumberOfTimesAPatternAppearsInAText() throws IOException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        String pattern = sc.nextLine();
        List<Integer> indices = Collections.synchronizedList(new ArrayList<>());
        int patternSize = pattern.length();

        IntStream.range(0, text.length())
                .parallel()
                .forEach(index -> {
                    if (text.charAt(index) == pattern.charAt(0)) {
                        if (text.substring(index, Math.min(index + patternSize, text.length())).equals(pattern)) {
                            indices.add(index);
                        }
                    }
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(indices.size());
        pw.flush();
    }
}
