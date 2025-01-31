package com.jdefossez.adventofcode.year2024.days.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {

    private static final Pattern ROBOT_PATTERN = Pattern.compile("^p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)$");

    public static void main(String[] args) throws IOException, URISyntaxException {


        Path path = Paths.get(Objects.requireNonNull(Day16.class.getClassLoader().getResource("input/2024/day_16_test.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> grid = initGrid(lines);

        Coord[] startAndEnd = initStartAndEnd(grid);
        Coord start = startAndEnd[0];
        Coord end = startAndEnd[1];

        System.out.printf("Start: %s, end: %s\n", start, end);
    }

    private static List<List<String>> initGrid(List<String> lines) {
        return lines.stream()
                    .map(line -> Arrays.stream(line.split("")).collect(Collectors.toList()))
                    .toList();
    }

    private static Coord[] initStartAndEnd(List<List<String>> grid) {
        Coord start = null;
        Coord end = null;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (grid.get(i).get(j).equals("S")) {
                    start = new Coord(j, i);
                } else if (grid.get(i).get(j).equals("E")) {
                    end = new Coord(j, i);
                }
            }
        }

        return List.of(start, end).toArray(new Coord[0]);
    }


}
