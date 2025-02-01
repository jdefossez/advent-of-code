package com.jdefossez.adventofcode.year2016.days;

import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day03 {

    public static void main(String[] args) throws IOException {

        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2016/03.txt"));

        Pattern p = Pattern.compile("^\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)$");
        List<List<Integer>> triangleList = input.stream().map(s -> {
                                                    Matcher m = p.matcher(s);
                                                    if (m.find()) {
                                                        return List.of(
                                                                Integer.parseInt(m.group(1)),
                                                                Integer.parseInt(m.group(2)),
                                                                Integer.parseInt(m.group(3)));
                                                    }
                                                    throw new InvalidInputDataException("the line " + s + " is not a valid triangle definition");
                                                })
                                                .toList();
        System.out.println("Part 1: " + part1(triangleList));
        System.out.println("Part 2: " + part2(triangleList));

    }

    public static int part1(List<List<Integer>> triangles) {
        return (int) triangles.stream()
                              .map(Day03::isValidTriangle)
                              .filter(b -> b)
                              .count();
    }


    public static int part2(List<List<Integer>> triangles) {

        return (int) IntStream.range(0, triangles.size() / 3)
                              .map(i -> i * 3)
                              .mapToObj(i ->
                                      List.of(
                                              List.of(triangles.get(i).get(0), triangles.get(i + 1).get(0), triangles.get(i + 2).get(0)),
                                              List.of(triangles.get(i).get(1), triangles.get(i + 1).get(1), triangles.get(i + 2).get(1)),
                                              List.of(triangles.get(i).get(2), triangles.get(i + 1).get(2), triangles.get(i + 2).get(2))
                                      )
                              )
                              .flatMap(List::stream)
                              .map(Day03::isValidTriangle)
                              .filter(b -> b)
                              .count();
    }


    public static boolean isValidTriangle(List<Integer> sides) {
        List<Integer> sorted = sides.stream().sorted().toList();
        return sorted.getLast() < sorted.getFirst() + sorted.get(1);
    }
}
