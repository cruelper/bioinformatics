package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ComputeTheHammingDistanceBetweenTwoStrings extends Intro01.FindAPositionInAGenomeMinimizingTheSkew {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ComputeTheHammingDistanceBetweenTwoStrings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ComputeTheHammingDistanceBetweenTwoStrings() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text1 = sc.nextLine();
        String text2 = sc.nextLine();

        int count = (int) IntStream.range(0, text1.length())
                .parallel()
                .filter(i -> text1.charAt(i) != text2.charAt(i))
                .count();

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(count);
        pw.flush();
    }
}
