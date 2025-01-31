package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Day03 {

    public static void main(String[] args) throws IOException {

        // read file day02 into a String
        String input = Files.readString(Path.of("src/main/resources/input/2015/day03"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(String input) {
        String[] directions = input.split("");
        int x = 0;
        int y = 0;
        int houses = 1;

        Set<String> visited = new HashSet<String>();

        visited.add(x + "," + y);

        for (String direction : directions) {
            switch (direction) {
                case "^":
                    y++;
                    break;
                case "v":
                    y--;
                    break;
                case ">":
                    x++;
                    break;
                case "<":
                    x--;
                    break;
            }
            if (visited.add(x + "," + y)) {
                houses++;
            }
        }
        return houses;
    }

    public static int part2(String input) {
        String[] directions = input.split("");
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int houses = 1;

        Set<String> visited = new HashSet<String>();

        visited.add(x1 + "," + y1);

        for (int i = 0; i < directions.length; i++) {
            String direction = directions[i];
            int x = i % 2 == 0 ? x1 : x2;
            int y = i % 2 == 0 ? y1 : y2;

            switch (direction) {
                case "^":
                    y++;
                    break;
                case "v":
                    y--;
                    break;
                case ">":
                    x++;
                    break;
                case "<":
                    x--;
                    break;
            }
            if (visited.add(x + "," + y)) {
                houses++;
            }
            if (i % 2 == 0) {
                x1 = x;
                y1 = y;
            } else {
                x2 = x;
                y2 = y;
            }
        }
        return houses;
    }

}
