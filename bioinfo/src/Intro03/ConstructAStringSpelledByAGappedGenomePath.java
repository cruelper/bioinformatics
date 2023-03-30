package Intro03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class ConstructAStringSpelledByAGappedGenomePath {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ConstructAStringSpelledByAGappedGenomePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ConstructAStringSpelledByAGappedGenomePath() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName)).useLocale(Locale.US);
        List<String> leftPatterns = new ArrayList<>();
        List<String> rightPatterns = new ArrayList<>();

        int k = sc.nextInt();
        int d = sc.nextInt();
        while (sc.hasNext()) {
            String text = sc.next();
            int dividerIndex = text.indexOf('|');
            String left = text.substring(0, dividerIndex);
            String right = text.substring(dividerIndex + 1);

            leftPatterns.add(left);
            rightPatterns.add(right);
        }

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.print(leftPatterns.get(0));
        int leftPatternsSize = leftPatterns.size();
        IntStream.range(1, leftPatternsSize).forEach(i -> pw.print(leftPatterns.get(i).charAt(k - 1)));

        StringBuilder rightPart = new StringBuilder(rightPatterns.get(0));
        int rightPatternsSize = rightPatterns.size();
        IntStream.range(1, rightPatternsSize).forEach(i -> rightPart.append(rightPatterns.get(i).charAt(k - 1)));
        pw.print(rightPart.substring(rightPart.length() - k - d));

        pw.flush();
    }
}
