package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day01 {

    public static void main(String[] args) throws IOException {

        // read file day01 into a String
        String input = Files.readString(Path.of("src/main/resources/input/2015/day01"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(String input) {
        int floor = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                floor++;
            } else {
                floor--;
            }
        }
        return floor;
    }

    public static int part2(String input) {
        int floor = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                floor++;
            } else {
                floor--;
            }
            if (floor < 0) {
                return i+1;
            }
        }
        return floor;
    }

}
