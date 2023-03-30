package Intro01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class FindPatternsFormingClumpsInAString extends Intro01.FindAllOccurrencesOfAPatternInAString {
    public static String inputFileName = "/home/cruelper/university/bioinfo/src/input.txt";
    public static String outputFileName = "/home/cruelper/university/bioinfo/src/output.txt";

    public static void main(String[] args) {
        try {
            FindPatternsFormingClumpsInAString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FindPatternsFormingClumpsInAString() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inputFileName));
        String text = sc.nextLine();
        int k = sc.nextInt();
        int L = sc.nextInt();
        int t = sc.nextInt();
        Map<String, List<Integer>> patternToCount = new HashMap<>();
        List<String> sList = Collections.synchronizedList(new ArrayList<>());

        IntStream.range(0, text.length() - k)
                .forEach(index -> {
                    String pattern = text.substring(index, index + k);
                    if (!patternToCount.containsKey(pattern)) patternToCount.put(pattern, new ArrayList<>());
                    patternToCount
                            .get(pattern)
                            .add(index);
                });

        patternToCount.entrySet()
                .stream()
                .parallel()
                .filter(entry -> entry.getValue().size() >= t)
                .forEach(entry -> {
                    boolean isExists = IntStream.range(0, entry.getValue().size() - t + 1)
                            .anyMatch(i -> entry.getValue().get(i + t - 1) - entry.getValue().get(i) <= L - k);
                    if (isExists) sList.add(entry.getKey());
                });

        PrintWriter pw = new PrintWriter(outputFileName);
        sList.forEach(i -> pw.print(i + " "));
        pw.flush();
    }
}
