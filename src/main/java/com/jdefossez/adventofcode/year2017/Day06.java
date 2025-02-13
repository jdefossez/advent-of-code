package com.jdefossez.adventofcode.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {

    public static void main(String[] args) throws IOException {

        String stacks = Files.readString(Path.of("src/main/resources/input/2017/06.txt"));

        System.out.println("Part 1: " + part1(stacks));
        System.out.println("Part 2: " + part2(stacks));

    }

    public static int part1(String input) {
        List<Integer> stacks = Arrays.stream(input.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());

        List<String> visitedStacks = new ArrayList<>();
        visitedStacks.add(String.join(" ", stacks.stream().map(Object::toString).toList()));

        int count = 0;
        while (true) {
            count++;
            int max = Collections.max(stacks);
            int indexMax = stacks.indexOf(max);
            stacks.set(indexMax, 0);

            for (int i = indexMax + 1; i < indexMax + 1 + max; i++) {
                stacks.set(i % stacks.size(), 1 + stacks.get(i % stacks.size()));
            }

            String newStackStr = String.join(" ", stacks.stream().map(Object::toString).toList());

            if (visitedStacks.contains(newStackStr)) {
                return count;
            }
            visitedStacks.add(newStackStr);
        }
    }


    public static int part2(String input) {
        List<Integer> stacks = Arrays.stream(input.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());

        List<String> visitedStacks = new ArrayList<>();
        visitedStacks.add(String.join(" ", stacks.stream().map(Object::toString).toList()));

        int count = 0;
        String newStackStr = "";

        while (true) {
            count++;
            int max = Collections.max(stacks);
            int indexMax = stacks.indexOf(max);
            stacks.set(indexMax, 0);

            for (int i = indexMax + 1; i < indexMax + 1 + max; i++) {
                stacks.set(i % stacks.size(), 1 + stacks.get(i % stacks.size()));
            }

            newStackStr = String.join(" ", stacks.stream().map(Object::toString).toList());

            if (visitedStacks.contains(newStackStr)) {
                break;
            }
            visitedStacks.add(newStackStr);
        }

        visitedStacks.clear();
        visitedStacks.add(newStackStr);

        count = 0;

        while (true) {
            count++;
            int max = Collections.max(stacks);
            int indexMax = stacks.indexOf(max);
            stacks.set(indexMax, 0);

            for (int i = indexMax + 1; i < indexMax + 1 + max; i++) {
                stacks.set(i % stacks.size(), 1 + stacks.get(i % stacks.size()));
            }

            newStackStr = String.join(" ", stacks.stream().map(Object::toString).toList());

            if (visitedStacks.contains(newStackStr)) {
                break;
            }
            visitedStacks.add(newStackStr);
        }

        return count;
    }
}
