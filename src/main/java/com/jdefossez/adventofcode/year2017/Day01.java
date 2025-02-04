package com.jdefossez.adventofcode.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

    public static void main(String[] args) throws IOException {

        List<String> rows = Files.readAllLines(Path.of("src/main/resources/input/2017/02.txt"));

        System.out.println("Part 1: " + part1(rows));
        System.out.println("Part 2: " + part2(rows));

    }

    public static int part1(List<String> rows) {
        return rows.stream()
                   .mapToInt(Day01::solveOneLinePart1).sum();

    }

    public static int solveOneLinePart1(String line) {
        IntSummaryStatistics summary = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).collect(Collectors.summarizingInt(s -> s));
        return summary.getMax() - summary.getMin();
    }


    public static int part2(List<String> rows) {
        return rows.stream().mapToInt(Day01::solveOneLinePart2).peek(System.out::println).sum();
    }

    public static int solveOneLinePart2(String line) {
        System.out.println(line);
        List<Integer> l = Arrays.stream(line.split("\\s+"))
                                .map(Integer::parseInt)
                                .sorted(Comparator.comparingInt(a -> -a))
                                .toList();

        for (int iNum = 0; iNum < l.size() - 1; iNum++) {
            for (Integer denom : l.subList(iNum + 1, l.size())) {
                int num = l.get(iNum);
                if (num % denom == 0) {
                    return num / denom;
                }
            }
        }
        return 0;
    }
}
