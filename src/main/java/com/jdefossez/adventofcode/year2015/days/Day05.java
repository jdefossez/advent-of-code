package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day05 {

    public static void main(String[] args) throws IOException {

        // read file day04 into a String
        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2015/day05"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {
        int niceStrings = 0;
        for (String line : input) {
            if (isNicePart1(line)) {
                niceStrings++;
            }
        }
        return niceStrings;
    }

    public static int part2(List<String> input) {
        int niceStrings = 0;
        for (String line : input) {
            if (isNicePart2(line)) {
                niceStrings++;
            }
        }
        return niceStrings;
    }

    public static boolean isNicePart1(String line) {
        int vowels = 0;
        boolean doubleLetter = false;
        boolean forbidden = false;

        forbidden = line.contains("ab") || line.contains("cd") || line.contains("pq") || line.contains("xy");
        doubleLetter = line.matches(".*(.)\\1.*");
        vowels = line.replaceAll("[^aeiou]", "").length();

        return !forbidden && doubleLetter && vowels >= 3;
    }

    public static boolean isNicePart2(String line) {
        boolean pair = false;
        boolean repeat = false;

        pair = line.matches(".*(..).*\\1.*");
        repeat = line.matches(".*(.).\\1.*");

        return pair && repeat;
    }
}
