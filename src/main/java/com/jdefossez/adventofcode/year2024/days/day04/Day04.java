package com.jdefossez.adventofcode.year2024.days.day04;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day04.class.getClassLoader().getResource("input/2024/day_04.txt")).toURI());

        String[] data =
                Files.lines(path)
                     .peek(System.out::println)
                     //.map(s -> s.split(""))
                     .toArray(String[]::new);

        var countHPos = Arrays.stream(data).mapToInt(s -> (s.length() - s.replace("XMAS", "").length()) / 4).sum();
        var countHNeg = Arrays.stream(data).mapToInt(s -> (s.length() - s.replace("SAMX", "").length()) / 4).sum();

        String[] data90Clockwise = IntStream.range(0, data[0].length())
                                            .mapToObj(x -> Arrays.stream(data).map(datum -> String.valueOf(datum.charAt(x)))
                                                                 .collect(Collectors.joining()))
                                            .toArray(String[]::new);

        System.out.printf("Result 1 : %s%n", countHPos);
        System.out.printf("Result 1 : %s%n", countHNeg);

//        Arrays.stream(data90Clockwise).forEach(System.out::println);

        var countVPos = Arrays.stream(data90Clockwise).mapToInt(s -> (s.length() - s.replace("XMAS", "").length()) / 4).sum();
        var countVNeg = Arrays.stream(data90Clockwise).mapToInt(s -> (s.length() - s.replace("SAMX", "").length()) / 4).sum();

        System.out.printf("Result 1 : %s%n", countVPos);
        System.out.printf("Result 1 : %s%n", countVNeg);

        String[] data45CW = getDiagonals45CW(data);
        Arrays.stream(data45CW).forEach(System.out::println);

        var countDiag45CWPos = Arrays.stream(data45CW).mapToInt(s -> (s.length() - s.replace("XMAS", "").length()) / 4).sum();
        var countDiag45CWNeg = Arrays.stream(data45CW).mapToInt(s -> (s.length() - s.replace("SAMX", "").length()) / 4).sum();

        System.out.printf("Result 1 : %s%n", countDiag45CWPos);
        System.out.printf("Result 1 : %s%n", countDiag45CWNeg);

        String[] data45CCW = getDiagonals45CCW(data);
        Arrays.stream(data45CCW).forEach(System.out::println);

        var countDiag45CCWPos = Arrays.stream(data45CCW).mapToInt(s -> (s.length() - s.replace("XMAS", "").length()) / 4).sum();
        var countDiag45CCWNeg = Arrays.stream(data45CCW).mapToInt(s -> (s.length() - s.replace("SAMX", "").length()) / 4).sum();

        System.out.printf("Result 1 : %s%n", countDiag45CWPos);
        System.out.printf("Result 1 : %s%n", countDiag45CWNeg);

        System.out.println("Result 1: " + (countHPos + countHNeg + countVPos + countVNeg + countDiag45CWPos + countDiag45CWNeg + countDiag45CCWPos + countDiag45CCWNeg));

    }

    private static String[] getDiagonals45CW(String[] input) {
        var rowCount = input.length;
        var colCount = input[0].length();

        var diagonalsCount = rowCount + colCount - 1;

        List<List<String>> diagonals = new ArrayList<>(diagonalsCount);
        for (int i = 0; i < diagonalsCount; i++) {
            diagonals.add(new ArrayList<>());
        }

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                var indexDiagonal = j - i + rowCount - 1;
                diagonals.get(indexDiagonal).add(String.valueOf(input[i].charAt(j)));
            }
        }

        String[] result = new String[diagonalsCount];
        for (int i = 0; i < diagonalsCount; i++) {
            result[i] = String.join("", diagonals.get(i));
        }

        return result;
    }

    private static String[] getDiagonals45CCW(String[] input) {
        var rowCount = input.length;
        var colCount = input[0].length();

        var diagonalsCount = rowCount + colCount - 1;

        List<List<String>> diagonals = new ArrayList<>(diagonalsCount);
        for (int i = 0; i < diagonalsCount; i++) {
            diagonals.add(new ArrayList<>());
        }

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                var indexDiagonal = j + i;
                diagonals.get(indexDiagonal).add(String.valueOf(input[i].charAt(j)));
            }
        }

        String[] result = new String[diagonalsCount];
        for (int i = 0; i < diagonalsCount; i++) {
            result[i] = String.join("", diagonals.get(i));
        }

        return result;
    }


}
