package com.jdefossez.adventofcode.year2016.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day01 {

    public static void main(String[] args) throws IOException {

        // read file day01 into a String
        String input = Files.readString(Path.of("src/main/resources/input/2016/01.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(String input) {

        String[] instructions = input.split(", ");
        int x = 0;
        int y = 0;
        int direction = 0;

        for (String instruction : instructions) {
            char turn = instruction.charAt(0);
            int distance = Integer.parseInt(instruction.substring(1));
            if (turn == 'R') {
                direction = (direction + 1) % 4;
            } else {
                direction = (direction + 3) % 4;
            }

            switch (direction) {
                case 0:
                    y += distance;
                    break;
                case 1:
                    x += distance;
                    break;
                case 2:
                    y -= distance;
                    break;
                case 3:
                    x -= distance;
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static int part2(String input) {

        String[] instructions = input.split(", ");
        int x = 0;
        int y = 0;
        int direction = 0;

        Map<Integer, Set<Integer>> visited = new HashMap<>();

        for (String instruction : instructions) {
            char turn = instruction.charAt(0);
            int distance = Integer.parseInt(instruction.substring(1));
            if (turn == 'R') {
                direction = (direction + 1) % 4;
            } else {
                direction = (direction + 3) % 4;
            }

            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 0:
                        y++;
                        break;
                    case 1:
                        x++;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x--;
                }
                visited.putIfAbsent(y, new HashSet<>());
                if (visited.get(y).contains(x)) {
                    return Math.abs(x) + Math.abs(y);
                } else {
                    visited.get(y).add(x);
                }
            }
        }

        return -1;
    }

}
