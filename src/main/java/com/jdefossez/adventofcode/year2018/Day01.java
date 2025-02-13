package com.jdefossez.adventofcode.year2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day01 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2018/01.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {
        return input.stream().mapToInt(Integer::parseInt).sum();
    }


    public static int part2(List<String> input) {

        List<Integer> offset = input.stream().map(Integer::parseInt).toList();

        List<Integer> visited = new ArrayList<>();
        visited.add(0);

        int current = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int v = current + offset.get(i % offset.size());
            current = v;
            if (visited.contains(v)) {
                return v;
            }
            visited.add(v);
        }
        return 0;
    }

}
