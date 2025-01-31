package com.jdefossez.adventofcode.year2024.days.day15;

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

public class Day15 {

    private static final Pattern ROBOT_PATTERN = Pattern.compile("^p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)$");

    public static void main(String[] args) throws IOException, URISyntaxException {


        Path path = Paths.get(Objects.requireNonNull(Day15.class.getClassLoader().getResource("input/2024/day_15.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> grid = initGrid(lines);
        Coord robotPosition = getInitRobotPosition(grid);
//        System.out.println(robotPosition);
//        for (List<String> line : grid) {
//            System.out.println(String.join("", line));
//        }

        List<String> moveAttempts = initMoveAttempts(lines);

        for (String moveAttempt : moveAttempts) {
            switch (moveAttempt) {
                case "^":
                    attemptMoveToNorth(grid, robotPosition);
                    break;
                case ">":
                    attemptMoveToEast(grid, robotPosition);
                    break;
                case "v":
                    attemptMoveToSouth(grid, robotPosition);
                    break;
                case "<":
                    attemptMoveToWest(grid, robotPosition);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unrecognized move attempt %s", moveAttempt));
            }

//            for (List<String> line : grid) {
//                System.out.println(String.join("", line));
//            }

        }

        long sum = computeGPSCoordinatesSum(grid);
        System.out.printf("Sum of GPS coordinates is %s", sum);
    }

    private static long computeGPSCoordinatesSum(List<List<String>> grid) {
        long sum = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                String cell = grid.get(i).get(j);
                if (cell.equals("O")) {
                    sum += (100L * i + j);
                }
            }
        }
        return sum;
    }

    private static void attemptMoveToNorth(List<List<String>> grid, Coord robotPosition) {
        // tant que la case au nord est différente de . (vide) ou qu'on n'est pas sur la première ligne, on continue
        int yFreeCell = robotPosition.getY();

        while (yFreeCell > 0) {
            if (grid.get(yFreeCell).get(robotPosition.getX()).equals(".")) {
                break;
            }
            if (grid.get(yFreeCell).get(robotPosition.getX()).equals("#")) {
                return;
            }
            yFreeCell--;
        }

        if (yFreeCell > 0) {
            for (int i = yFreeCell; i < robotPosition.getY(); i++) {
                grid.get(i).set(robotPosition.getX(), grid.get(i + 1).get(robotPosition.getX()));
            }
            grid.get(robotPosition.getY()).set(robotPosition.getX(), ".");
            robotPosition.moveToNorth();
        }
    }


    private static void attemptMoveToEast(List<List<String>> grid, Coord robotPosition) {
        // tant que la case à l'est est différente de . (vide) ou qu'on n'est pas sur la dernière colonne, on continue
        int xFreeCell = robotPosition.getX();

        while (xFreeCell < grid.getFirst().size()) {
            if (grid.get(robotPosition.getY()).get(xFreeCell).equals(".")) {
                break;
            }
            if (grid.get(robotPosition.getY()).get(xFreeCell).equals("#")) {
                return;
            }
            xFreeCell++;
        }

        if (xFreeCell < grid.getFirst().size()) {
            for (int i = xFreeCell; i > robotPosition.getX(); i--) {
                grid.get(robotPosition.getY()).set(i, grid.get(robotPosition.getY()).get(i - 1));
            }
            grid.get(robotPosition.getY()).set(robotPosition.getX(), ".");
            robotPosition.moveToEast();
        }
    }

    private static void attemptMoveToSouth(List<List<String>> grid, Coord robotPosition) {
        // tant que la case au sud est différente de . (vide) ou qu'on n'est pas sur la dernière ligne, on continue
        int yFreeCell = robotPosition.getY();

        while (yFreeCell < grid.size()) {
            if (grid.get(yFreeCell).get(robotPosition.getX()).equals(".")) {
                break;
            }
            if (grid.get(yFreeCell).get(robotPosition.getX()).equals("#")) {
                return;
            }
            yFreeCell++;
        }

        if (yFreeCell < grid.size()) {
            for (int i = yFreeCell; i > robotPosition.getY(); i--) {
                grid.get(i).set(robotPosition.getX(), grid.get(i - 1).get(robotPosition.getX()));
            }
            grid.get(robotPosition.getY()).set(robotPosition.getX(), ".");
            robotPosition.moveToSouth();
        }
    }

    private static void attemptMoveToWest(List<List<String>> grid, Coord robotPosition) {
        // tant que la case à l'ouest est différente de . (vide) ou qu'on n'est pas sur la première colonne, on continue
        int xFreeCell = robotPosition.getX();

        while (xFreeCell > 0) {
            if (grid.get(robotPosition.getY()).get(xFreeCell).equals(".")) {
                break;
            }
            if (grid.get(robotPosition.getY()).get(xFreeCell).equals("#")) {
                return;
            }
            xFreeCell--;
        }

        if (xFreeCell > 0) {
            for (int i = xFreeCell; i < robotPosition.getX(); i++) {
                grid.get(robotPosition.getY()).set(i, grid.get(robotPosition.getY()).get(i + 1));
            }
            grid.get(robotPosition.getY()).set(robotPosition.getX(), ".");
            robotPosition.moveToWest();
        }
    }

    private static Coord getInitRobotPosition(List<List<String>> grid) {
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (grid.get(i).get(j).equals("@")) {
                    return new Coord(j, i);
                }
            }
        }
        return null;
    }

    private static List<List<String>> initGrid(List<String> lines) {
        return lines.stream()
                    .filter(line -> line.startsWith("#"))
                    .map(line -> Arrays.stream(line.split("")).collect(Collectors.toList()))
                    .toList();
    }

    private static List<String> initMoveAttempts(List<String> lines) {
        return lines.stream()
                    .dropWhile(line -> line.startsWith("#"))
                    .skip(1)
                    .flatMap(line -> Arrays.stream(line.split("")))
                    .toList();
    }


}
