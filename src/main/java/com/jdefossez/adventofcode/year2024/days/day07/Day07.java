package com.jdefossez.adventofcode.year2024.days.day07;

import com.jdefossez.adventofcode.year2024.days.day06.Coord;
import com.jdefossez.adventofcode.year2024.days.day06.Dir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day07 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Map<Coord, Set<Dir>> fullPath = new HashMap<>();

        Path path = Paths.get(Objects.requireNonNull(Day07.class.getClassLoader().getResource("input/2024/day_07.txt")).toURI());

        Long result = Files.lines(path).toList()
                           .stream()
                           .map(Equation::parse)
                           .filter(Equation::isSolvable)
                           .mapToLong(Equation::getResult)
                           .sum();

        System.out.printf("Result : %s\n", result);

    }

}
