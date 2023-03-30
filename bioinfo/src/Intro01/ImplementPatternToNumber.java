package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImplementPatternToNumber extends Intro01.GenerateTheDNeighborhoodOfAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            ImplementPatternToNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImplementPatternToNumber() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine()
                .replace('A', '0')
                .replace('C', '1')
                .replace('G', '2')
                .replace('T', '3');;

        PrintWriter pw = new PrintWriter(outputFileName);
        pw.println(Long.valueOf(text, 4));
        pw.flush();
    }
}
