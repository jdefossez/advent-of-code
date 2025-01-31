package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day06 {

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2015/day06"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {
        // Initialize the grid
        int[][] grid = new int[1000][1000];

        // Parse the input
        for (String line : input) {
            String[] parts = line.split(" ");
            if (line.startsWith("turn on")) {
                int[] start = parse(parts[2]);
                int[] end = parse(parts[4]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j] = 1;
                    }
                }
            }

            if (line.startsWith("turn off")) {
                int[] start = parse(parts[2]);
                int[] end = parse(parts[4]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j] = 0;
                    }
                }
            }

            if (line.startsWith("toggle")) {
                int[] start = parse(parts[1]);
                int[] end = parse(parts[3]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j] = grid[i][j] == 1 ? 0 : 1;
                    }
                }
            }
        }

        // Count the number of lights that are on
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                count += grid[i][j];
            }
        }

        return count;
    }

    public static int part2(List<String> input) {
        // Initialize the grid
        int[][] grid = new int[1000][1000];

        // Parse the input
        for (String line : input) {
            String[] parts = line.split(" ");
            if (line.startsWith("turn on")) {
                int[] start = parse(parts[2]);
                int[] end = parse(parts[4]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j]++;
                    }
                }
            }

            if (line.startsWith("turn off")) {
                int[] start = parse(parts[2]);
                int[] end = parse(parts[4]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j] = Math.max(0, grid[i][j] - 1);
                    }
                }
            }

            if (line.startsWith("toggle")) {
                int[] start = parse(parts[1]);
                int[] end = parse(parts[3]);
                for (int i = start[0]; i <= end[0]; i++) {
                    for (int j = start[1]; j <= end[1]; j++) {
                        grid[i][j] += 2;
                    }
                }
            }
        }

        // Count the number of lights that are on
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                count += grid[i][j];
            }
        }

        return count;
    }


    private static int[] parse(String s) {
        String[] parts = s.split(",");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }
}
