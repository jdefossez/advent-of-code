package com.jdefossez.adventofcode.year2024.days.day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Day20 {

    private static final int TARGET = 100;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day20.class.getClassLoader().getResource("input/2024/day_20.txt")).toURI());
        List<String> lines = Files.lines(path).toList();
        int height = lines.size();
        int width = lines.getFirst().length();

        // (x, y)
        var costs = initCosts(lines);

//        System.out.println(costs);
        System.out.println("Part 1 : " + part1(costs));

//        System.out.println(getCheatNeighborsPart2(new Coord(5, 7), 20));
//
        System.out.println("Part 2 : " + part2(costs)); // 285 pour l'exemple
    }

    private static long part1(LinkedHashMap<Coord, Integer> costs) {
        long count = 0;
        for (Coord c : costs.sequencedKeySet().reversed()) {
            for (Coord neighbor : getCheatNeighborsPart1(c)) {
                if (costs.containsKey(neighbor)
                        && costs.get(neighbor) <= costs.get(c) - 2 - TARGET) {
//                    System.out.println(neighbor + " to " + c + " is a possible cheat. " + (costs.get(c) - costs.get(neighbor) - 2) + " ps saved");
                    count++;
                }
            }
        }
        return count;
    }

    private static long part2(LinkedHashMap<Coord, Integer> costs) {
        Map<Coord, Set<Coord>> result = new HashMap<>();
        for (Coord c : costs.sequencedKeySet().reversed()) {
//            System.out.println(getCheatNeighborsPart2(c, 20));
            for (Coord neighbor : getCheatNeighborsPart2(c, 20)) {
                if (costs.containsKey(neighbor)
                        && costs.get(neighbor) <= costs.get(c) - hammingDistance(c, neighbor) - TARGET) {
//                    System.out.println(neighbor + " to " + c + " is a possible cheat. " + (costs.get(c) - costs.get(neighbor) - hammingDistance(c, neighbor)) + " ps saved");
                    result.putIfAbsent(neighbor, new HashSet<>());
                    result.get(neighbor).add(c);
                }
            }
        }
        return result.values().stream().mapToLong(Set::size).sum();
    }

    private static int hammingDistance(Coord c1, Coord c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    private static List<Coord> getCheatNeighborsPart1(Coord coord) {
        return List.of(
                new Coord(coord.getX() + 2, coord.getY()), // East
                new Coord(coord.getX() - 2, coord.getY()), // West
                new Coord(coord.getX(), coord.getY() + 2), // South
                new Coord(coord.getX(), coord.getY() - 2)  // North
        );
    }

    private static List<Coord> getCheatNeighborsPart2(Coord coord, int dist) {
        final int x = coord.getX();
        final int y = coord.getY();
//        for (int i = coord.getY() - dist; i <= coord.getY() + dist; i++) {
//            System.out.println("i " + i);
//            int offset = Math.abs(coord.getY() - i);
//            for (int j = coord.getX() - dist + offset; j <= coord.getX() + dist - offset; j++) {
//                System.out.printf("%d,%d, ", j, i);
//            }
//        }
//        System.out.println();
        return IntStream
                .rangeClosed(y - dist, y + dist)
                .boxed()
                .flatMap(i -> {
                    int offset = Math.abs(coord.getY() - i);
                    return IntStream.rangeClosed(x - dist + offset, x + dist - offset)
                                    .filter(j -> Math.abs(x - j) > 1 || Math.abs(y - i) > 1)
                                    .mapToObj(j -> new Coord(j, i));
                })
                .toList();

    }

    private static LinkedHashMap<Coord, Integer> initCosts(List<String> grid) {

        int height = grid.size();
        int width = grid.getFirst().length();

        // find start and end
        Coord start = new Coord();
        Coord end = new Coord();

        for (int y = 0; y < grid.size(); y++) {
            if (grid.get(y).contains("S")) {
                start.setX(grid.get(y).indexOf("S"));
                start.setY(y);
            } else if (grid.get(y).contains("E")) {
                end.setX(grid.get(y).indexOf("E"));
                end.setY(y);
            }
        }

        // move along the path and set the costs
        LinkedHashMap<Coord, Integer> costs = new LinkedHashMap<>();
        Coord current = start;
        Coord nextCoord = null;
        int cost = 0;
        costs.put(start, cost++);

        while (grid.get(current.getY()).charAt(current.getX()) != 'E') {
            // E
            if (current.getX() < width - 1
                    && (grid.get(current.getY()).charAt(current.getX() + 1) == '.' || grid.get(current.getY()).charAt(current.getX() + 1) == 'E')
                    && costs.get(new Coord(current.getX() + 1, current.getY())) == null) {
                nextCoord = new Coord(current.getX() + 1, current.getY());
            } else if (current.getX() > 0
                    && (grid.get(current.getY()).charAt(current.getX() - 1) == '.' || grid.get(current.getY()).charAt(current.getX() - 1) == 'E')
                    && costs.get(new Coord(current.getX() - 1, current.getY())) == null) {
                nextCoord = new Coord(current.getX() - 1, current.getY());
            } else if (current.getY() > 0
                    && (grid.get(current.getY() - 1).charAt(current.getX()) == '.' || grid.get(current.getY() - 1).charAt(current.getX()) == 'E')
                    && costs.get(new Coord(current.getX(), current.getY() - 1)) == null) {
                nextCoord = new Coord(current.getX(), current.getY() - 1);
            } else if (current.getY() < height - 1
                    && (grid.get(current.getY() + 1).charAt(current.getX()) == '.' || grid.get(current.getY() + 1).charAt(current.getX()) == 'E')
                    && costs.get(new Coord(current.getX(), current.getY() + 1)) == null) {
                nextCoord = new Coord(current.getX(), current.getY() + 1);
            }

            costs.put(nextCoord, cost++);
            current = nextCoord;
        }

        return costs;
    }

}
