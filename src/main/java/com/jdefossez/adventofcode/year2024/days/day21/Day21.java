package com.jdefossez.adventofcode.year2024.days.day21;

import com.jdefossez.adventofcode.year2024.days.day20.Coord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Day21 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day21.class.getClassLoader().getResource("input/2024/day_21_test.txt")).toURI());
        List<String> codes = Files.lines(path).toList();


        System.out.println("Part 1 : " + part1(codes));
    }

    private static long part1(List<String> codes) {

        return 0;
    }

    private static long part2() {

        return 0;
    }

    private static int hammingDistance(Coord c1, Coord c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

}
