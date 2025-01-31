package com.jdefossez.adventofcode.year2024.days.day02;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Day02 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var mapper = new Day02DataLineMapper();

        Path path = Paths.get(Objects.requireNonNull(Day02.class.getClassLoader().getResource("input/2024/day_02.txt")).toURI());
        List<Day02DataLine> inputData = Files.lines(path)
                                             .peek(System.out::println)
                                             .map(mapper::toObject)
                                             .peek(System.out::println)
                                             .toList();

        var result = inputData.stream().filter(Day02DataLine::isSafeA).count();

        System.out.printf("Result 1 : %s%n", result);

        result = inputData.stream().peek(System.out::println).filter(Day02DataLine::isSafeB).count();

        System.out.printf("Result 2 : %s%n", result);
    }
}
