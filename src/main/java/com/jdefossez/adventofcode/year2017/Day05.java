package com.jdefossez.adventofcode.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {

    public static void main(String[] args) throws IOException {

        List<String> instructions = Files.readAllLines(Path.of("src/main/resources/input/2017/05.txt"));

        System.out.println("Part 1: " + part1(instructions));
        System.out.println("Part 2: " + part2(instructions));

    }

    public static int part1(List<String> instructions) {

        List<Integer> stack = instructions.stream().map(Integer::parseInt).collect(Collectors.toList());

        int nextIndex = 0;
        int count = 0;
        while (nextIndex >= 0 && nextIndex < stack.size()) {
            int offset = stack.get(nextIndex);
            stack.set(nextIndex, offset + 1);
            nextIndex += offset;
            count++;
        }
        return count;
    }


    public static int part2(List<String> instructions) {
        List<Integer> stack = instructions.stream().map(Integer::parseInt).collect(Collectors.toList());

        int nextIndex = 0;
        int count = 0;
        while (nextIndex >= 0 && nextIndex < stack.size()) {
            int offset = stack.get(nextIndex);
            if (offset >= 3) {
                stack.set(nextIndex, offset - 1);
            } else {
                stack.set(nextIndex, offset + 1);
            }

            nextIndex += offset;
            count++;
        }
        return count;
    }
}
