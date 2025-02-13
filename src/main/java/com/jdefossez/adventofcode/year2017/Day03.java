package com.jdefossez.adventofcode.year2017;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Day03 {

    public static void main(String[] args) throws IOException {

        Integer input = 325489;

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(int input) {
        int squareRoot = immediateFirstGreaterSquare(input);
        int layerIndex = 1 + squareRoot / 2;
        int minPathLength = layerIndex - 1;

        int sideLength = layerIndex * 2 - 1;

        int c1 = (squareRoot * squareRoot) - (sideLength - 1) / 2;
        int c2 = c1 - 2 * ((sideLength - 1) / 2);
        int c3 = c2 - 2 * ((sideLength - 1) / 2);
        int c4 = c3 - 2 * ((sideLength - 1) / 2);

        int diff = Math.min(Math.abs(input - c1), Math.min(Math.abs(input - c2), Math.min(Math.abs(input - c3), Math.abs(input - c4))));

        return minPathLength + diff;
    }

    private static int immediateFirstGreaterSquare(int input) {
        return IntStream.iterate(1, i -> i + 2).filter(i -> i * i >= input).findFirst().getAsInt();
    }

    public static int part2(int input) {
        Map<Integer, Map<Integer, Integer>> grid = new HashMap<>();
        grid.put(0, new HashMap<>());
        grid.get(0).put(0, 1);

        int nextValue = 0;
        int layerIndex = 2;
        while (layerIndex < 1000) {

            int x = layerIndex - 1;
            for (int yv = -layerIndex + 2; yv <= layerIndex - 2; yv++) {
                grid.putIfAbsent(yv, new HashMap<>());
                nextValue = getSumNeighbors(grid, yv, x);
                if (nextValue > input) {
                    return nextValue;
                }
                grid.get(yv).put(x, nextValue);
            }

            int y = layerIndex - 1;
            for (int xv = layerIndex - 1; xv >= -layerIndex + 2; xv--) {
                grid.putIfAbsent(y, new HashMap<>());
                nextValue = getSumNeighbors(grid, y, xv);
                if (nextValue > input) {
                    return nextValue;
                }
                grid.get(y).put(xv, nextValue);
            }

            x = -layerIndex + 1;
            for (int yv = layerIndex - 1; yv >= -layerIndex + 2; yv--) {
                grid.putIfAbsent(yv, new HashMap<>());
                nextValue = getSumNeighbors(grid, yv, x);
                if (nextValue > input) {
                    return nextValue;
                }
                grid.get(yv).put(x, nextValue);
            }

            y = -layerIndex + 1;
            for (int xv = -layerIndex + 1; xv <= layerIndex - 1; xv++) {
                grid.putIfAbsent(y, new HashMap<>());
                nextValue = getSumNeighbors(grid, y, xv);
                if (nextValue > input) {
                    return nextValue;
                }
                grid.get(y).put(xv, nextValue);
            }

            layerIndex++;
        }
        return 0;
    }

    private static int getSumNeighbors(Map<Integer, Map<Integer, Integer>> grid, int y, int x) {
        return grid.getOrDefault(y - 1, new HashMap<Integer, Integer>()).getOrDefault(x - 1, 0)
                + grid.getOrDefault(y - 1, new HashMap<Integer, Integer>()).getOrDefault(x, 0)
                + grid.getOrDefault(y - 1, new HashMap<Integer, Integer>()).getOrDefault(x + 1, 0)
                + grid.getOrDefault(y, new HashMap<Integer, Integer>()).getOrDefault(x - 1, 0)
                + grid.getOrDefault(y, new HashMap<Integer, Integer>()).getOrDefault(x + 1, 0)
                + grid.getOrDefault(y + 1, new HashMap<Integer, Integer>()).getOrDefault(x - 1, 0)
                + grid.getOrDefault(y + 1, new HashMap<Integer, Integer>()).getOrDefault(x, 0)
                + grid.getOrDefault(y + 1, new HashMap<Integer, Integer>()).getOrDefault(x + 1, 0);
    }

}
