package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day04 {

    public static void main(String[] args) throws IOException {

        // read file day04 into a String
        String input = Files.readString(Path.of("src/main/resources/input/2015/day04"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(String input) {
        int i = 0;
        while (true) {
            String hash = MD5.getMD5(input + i);
            if (hash.startsWith("00000")) {
                return i;
            }
            i++;
        }
    }

    public static int part2(String input) {
        int i = 0;
        while (true) {
            String hash = MD5.getMD5(input + i);
            if (hash.startsWith("000000")) {
                return i;
            }
            i++;
        }
    }
}
