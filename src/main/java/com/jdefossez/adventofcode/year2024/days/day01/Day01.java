package com.jdefossez.adventofcode.year2024.days.day01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day01 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var mapper = new Day01DataLineMapper();

        Path path = Paths.get(Objects.requireNonNull(Day01.class.getClassLoader().getResource("input/2024/day_01_a.txt")).toURI());
        List<Day01DataLine> inputData = Files.lines(path)
                                             .map(mapper::toObject)
                                             .toList();

        var sortedListFirst = inputData.stream().map(Day01DataLine::first).sorted().toList();
        var sortedListSecond = inputData.stream().map(Day01DataLine::second).sorted().toList();

        int sumOfDifferences = IntStream.range(0, sortedListFirst.size())
                                        .map(i -> Math.abs(sortedListFirst.get(i) - sortedListSecond.get(i)))
                                        .sum();

        System.out.printf("Result 1 : %s%n", sumOfDifferences);

        Map<Integer, Long> mapSecondList = sortedListSecond.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        int similarityScore = sortedListFirst.stream().mapToInt(i -> (int) (i * mapSecondList.getOrDefault(i, 0L))).sum();

        System.out.printf("Result 2 : %s%n", similarityScore);
    }
}
