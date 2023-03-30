package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindAPositionInAGenomeMinimizingTheSkew extends Intro01.FindPatternsFormingClumpsInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindAPositionInAGenomeMinimizingTheSkew();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindAPositionInAGenomeMinimizingTheSkew() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();

        List<Integer> skew = text.chars()
                .mapToObj(c -> c == 'C' ? -1 : (c == 'G' ? 1 : 0))
                .collect(Collectors.toList());

        AtomicReference<Integer> minSkew = new AtomicReference<>(skew.get(0));

        IntStream.range(1, skew.size())
                .forEach(i -> {
                    int curSkew = skew.get(i) + skew.get(i - 1);
                    skew.set(i, curSkew);
                    minSkew.set(Math.min(minSkew.get(), curSkew));
                });

        int[] indices = IntStream.range(0, skew.size())
                .filter(i -> Objects.equals(skew.get(i), minSkew.get()))
                .toArray();

        PrintWriter pw = new PrintWriter(outputFileName);
        Arrays.stream(indices).forEach(i -> pw.print(i + 1 + " "));
        pw.flush();
    }
}
