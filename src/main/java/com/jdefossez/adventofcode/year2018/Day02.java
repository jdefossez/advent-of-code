package com.jdefossez.adventofcode.year2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day02 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2018/02.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static long part1(List<String> input) {

        long count2 = input.stream().filter(s -> hasN(s, 2)).count();
        long count3 = input.stream().filter(s -> hasN(s, 3)).count();

        return count2 * count3;
    }

    private static boolean hasN(String input, long n) {
        return Arrays.stream(input.split("")).collect(Collectors.groupingBy(s -> s, Collectors.counting())).containsValue(n);
    }


    public static String part2(List<String> input) {

        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size(); j++) {
                String s1 = input.get(i);
                String s2 = input.get(j);
                if (hamming(s1, s2) == 1) {
                    return IntStream.range(0, s1.length())
                                    .filter(k -> s1.charAt(k) == s2.charAt(k))
                                    .mapToObj(k -> String.valueOf(s1.charAt(k)))
                                    .collect(Collectors.joining());
                }
            }
        }
        return "Not found";
    }

    private static int hamming(String s1, String s2) {
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("Les chaînes doivent avoir la même longueur.");
        }

        return (int) IntStream.range(0, s1.length())
                              .filter(i -> s1.charAt(i) != s2.charAt(i))
                              .count();
    }

}
