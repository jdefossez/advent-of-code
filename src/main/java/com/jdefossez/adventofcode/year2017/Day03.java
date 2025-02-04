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

        System.out.printf("squareRoot: %d, layerIndex: %d, maxPathLength: %d, sideLength: %d, c1: %d, c2: %d, c3: %d, c4: %d, diff: %d\n",
                squareRoot, layerIndex, minPathLength, sideLength, c1, c2, c3, c4, diff);

        return minPathLength + diff;
    }

    private static int immediateFirstGreaterSquare(int input) {
        return IntStream.iterate(1, i -> i + 2).filter(i -> i * i >= input).findFirst().getAsInt();
    }

    public static int part2(int input) {
        Map<Integer, Map<Integer, Integer>> grid = new HashMap<>();
        grid.put(0, new HashMap<>());
        grid.get(0).put(0, 1);

        var o = IntStream.iterate(2, i -> i + 1).mapToObj(i -> {
                             int squareRoot = immediateFirstGreaterSquare(i);
                             int layerIndex = 1 + squareRoot / 2;
                             int sideLength = layerIndex * 2 - 1;

                             int c1 = (squareRoot * squareRoot) - (sideLength - 1) / 2; // S
                             int c2 = c1 - 2 * ((sideLength - 1) / 2); // W
                             int c3 = c2 - 2 * ((sideLength - 1) / 2); // N
                             int c4 = c3 - 2 * ((sideLength - 1) / 2); // E

                             int x = 0;
                             if (Math.abs(c4 - i) <= (sideLength - 1) / 2) {
                                 x = layerIndex - 1;
                             } else if (Math.abs(c2 - i) <= (sideLength - 1) / 2) {
                                 x = 1 - layerIndex;
                             }

                             System.out.printf("i: %d ==> squareRoot: %d, layerIndex: %d, sideLength: %d, x: %d\n", i, squareRoot, layerIndex, sideLength, x);
                             return new int[]{0, 0};
                         })
                         .limit(20)
                         .toList();
        ;
        return 0;
    }

}
