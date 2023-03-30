package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ImplementNumberToPattern extends Intro01.ImplementPatternToNumber {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ImplementNumberToPattern();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImplementNumberToPattern() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        int num = sc.nextInt();
        int k = sc.nextInt();

        StringBuilder numIn4RadixStrBuilder = new StringBuilder(Integer.toString(num, 4));
        int leadingZerosCount = k - numIn4RadixStrBuilder.length();
        IntStream.range(0, leadingZerosCount)
                .forEach(j -> numIn4RadixStrBuilder.insert(0, 0));

        // A=0, C=1, G=2, T=3
        String numIn4RadixStr = numIn4RadixStrBuilder.toString()
                .replace('0', 'A')
                .replace('1', 'C')
                .replace('2', 'G')
                .replace('3', 'T');

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(numIn4RadixStr);
        pw.flush();
    }
}
