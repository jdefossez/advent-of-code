package com.jdefossez.adventofcode.year2024.days.day18;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {

    private static final int SIZE = 71;
    private static final int NB_BYTE = 1024;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day18.class.getClassLoader().getResource("input/2024/day_18.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<Coord> coords = lines.stream()
                                  .map(s -> s.split(","))
                                  .map(a -> new Coord(Integer.parseInt(a[0]), Integer.parseInt(a[1])))
                                  .toList();

        System.out.println("Part 1 : " + part1(coords.subList(0, NB_BYTE)));

        System.out.println("Part 2 : " + part2(coords));
    }

    private static int part1(List<Coord> coords) {
        int[][] grid = new int[SIZE][SIZE];

        coords.forEach(c -> {
            grid[c.getY()][c.getX()] = 1;
        });
        printGrid(grid);

        List<Coord> path = bfs(grid, new Coord(0, 0), new Coord(SIZE - 1, SIZE - 1));

        assert path != null;
        path.forEach(c -> {
            grid[c.getY()][c.getX()] = 2;
        });

        printGrid(grid);

        return path.size() - 1;
    }

    private static Coord part2(List<Coord> coords) {
        int[][] grid = new int[SIZE][SIZE];

        coords.subList(0, NB_BYTE).forEach(c -> {
            grid[c.getY()][c.getX()] = 1;
        });

        int indexFirstBlocking = NB_BYTE - 1;
        List<Coord> path = bfs(grid, new Coord(0, 0), new Coord(SIZE - 1, SIZE - 1));
        while (path != null) {
            indexFirstBlocking++;
            Coord nextByteCoords = coords.get(indexFirstBlocking);
            grid[nextByteCoords.getY()][nextByteCoords.getX()] = 1;
            path = bfs(grid, new Coord(0, 0), new Coord(SIZE - 1, SIZE - 1));
        }

        return coords.get(indexFirstBlocking);
    }

    private static void printGrid(int[][] grid) {
        for (int[] ints : grid) {
            System.out.println(Arrays.stream(ints)
                                     .mapToObj(c -> switch (c) {
                                         case 1 -> "#";
                                         case 2 -> "O";
                                         default -> ".";
                                     })
                                     .collect(Collectors.joining("")));
        }
    }

    private static List<Coord> bfs(int[][] grid, Coord source, Coord target) {
        List<Coord> fifo = new ArrayList<>();
        fifo.add(source);

        Map<Coord, Coord> visited = new HashMap<>();
        visited.put(source, null);

        while (!fifo.isEmpty()) {
            Coord crtNode = fifo.removeFirst();

            if (crtNode.equals(target)) {
                List<Coord> path = new ArrayList<>();
                while (crtNode != null) {
                    path.add(crtNode);
                    crtNode = visited.get(crtNode);
                }
                return path.reversed();
            }

            for (Coord neighbor : getNeighbors(grid, crtNode)) {
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, crtNode);
                    fifo.add(neighbor);
                }
            }
        }
        return null;
    }

    private static List<Coord> getNeighbors(int[][] grid, Coord coord) {
        List<Coord> neighbors = new ArrayList<>();
        // E ?
        if (coord.getX() < grid[coord.getY()].length - 1
                && grid[coord.getY()][coord.getX() + 1] == 0) {
            neighbors.add(new Coord(coord.getX() + 1, coord.getY()));
        }
        // W ?
        if (coord.getX() > 0 && grid[coord.getY()][coord.getX() - 1] == 0) {
            neighbors.add(new Coord(coord.getX() - 1, coord.getY()));
        }
        // N ?
        if (coord.getY() > 0 && grid[coord.getY() - 1][coord.getX()] == 0) {
            neighbors.add(new Coord(coord.getX(), coord.getY() - 1));
        }
        // S ?
        if (coord.getY() < grid.length - 1 && grid[coord.getY() + 1][coord.getX()] == 0) {
            neighbors.add(new Coord(coord.getX(), coord.getY() + 1));
        }

        return neighbors;
    }

}
