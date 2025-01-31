package com.jdefossez.adventofcode.year2016.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day02 {

    public static void main(String[] args) throws IOException {

        // read file day01 into a String
        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2016/02.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {

        int start = 5; // 95549
        StringBuilder code = new StringBuilder();
        for (String instructions : input) {
            start = decodeOnePart1(instructions, start);
            code.append(start);
        }
        return Integer.parseInt(code.toString());
    }

    private static int decodeOnePart1(String instructions, int start) {
        int[][] keypad = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int xStart = (start - 1) % 3;
        int yStart = (start - 1) / 3;

        for (char instruction : instructions.toCharArray()) {
            switch (instruction) {
                case 'U':
                    yStart = Math.max(0, yStart - 1);
                    break;
                case 'D':
                    yStart = Math.min(2, yStart + 1);
                    break;
                case 'L':
                    xStart = Math.max(0, xStart - 1);
                    break;
                case 'R':
                    xStart = Math.min(2, xStart + 1);
                    break;
            }
        }
//        System.out.println(start + " " + yStart + ", " + xStart);
        return keypad[yStart][xStart];
    }

    public static String part2(List<String> input) {

        int start = 5;
        StringBuilder code = new StringBuilder();
        for (String instructions : input) {
            start = decodeOnePart2(instructions, start);
            code.append(Integer.toString(start, 16));
        }
        return code.toString().toUpperCase();
    }

    private static int decodeOnePart2(String instructions, int start) {
        int[][] keypad = {
                {0, 0, 1, 0, 0},
                {0, 2, 3, 4, 0},
                {5, 6, 7, 8, 9},
                {0, 0xA, 0xB, 0xC, 0},
                {0, 0, 0xD, 0, 0}
        };

        int yStart = 0;
        int xStart = 0;

        for (int y = 0; y < keypad.length; y++) {
            for (int x = 0; x < keypad[y].length; x++) {
                if (keypad[y][x] == start) {
                    yStart = y;
                    xStart = x;
                }
            }
        }

        for (char instruction : instructions.toCharArray()) {
            switch (instruction) {
                case 'U':
                    yStart = Math.max(0, yStart - 1);
                    if (keypad[yStart][xStart] == 0) {
                        yStart++;
                    }
                    break;
                case 'D':
                    yStart = Math.min(4, yStart + 1);
                    if (keypad[yStart][xStart] == 0) {
                        yStart--;
                    }
                    break;
                case 'L':
                    xStart = Math.max(0, xStart - 1);
                    if (keypad[yStart][xStart] == 0) {
                        xStart++;
                    }
                    break;
                case 'R':
                    xStart = Math.min(4, xStart + 1);
                    if (keypad[yStart][xStart] == 0) {
                        xStart--;
                    }
                    break;
            }
        }

        System.out.println(start + " " + yStart + ", " + xStart);
        return keypad[yStart][xStart];
    }
}
