package com.jdefossez.adventofcode.year2024.days.day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15_2 {

    public static void main(String[] args) throws IOException, URISyntaxException {


        Path path = Paths.get(Objects.requireNonNull(Day15_2.class.getClassLoader().getResource("input/2024/day_15.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> grid = initGrid(lines);
        Coord robotPosition = getInitRobotPosition(grid);
        System.out.println(robotPosition);
        for (List<String> line : grid) {
            System.out.println(String.join("", line));
        }

        List<String> moveAttempts = initMoveAttempts(lines);

        for (String moveAttempt : moveAttempts) {
            System.out.println(moveAttempt);
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

            for (List<String> line : grid) {
                System.out.println(String.join("", line));
            }

        }

        long sum = computeGPSCoordinatesSum(grid);
        System.out.printf("Sum of GPS coordinates is %s", sum);
    }

    private static long computeGPSCoordinatesSum(List<List<String>> grid) {
        long sum = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                String cell = grid.get(i).get(j);
                if (cell.equals("[")) {
                    sum += (100L * i + j);
                }
            }
        }
        return sum;
    }

    private static void attemptMoveToNorth(List<List<String>> grid, Coord robotPosition) {
        if (canMoveNorth(grid, robotPosition)) {
            System.out.println("Can move to north");
            moveNorth(grid, robotPosition);
            robotPosition.moveToNorth();
        } else {
            System.out.println("Cannot move to north");
        }
    }


    private static void attemptMoveToEast(List<List<String>> grid, Coord robotPosition) {
        // tant que la case à l'est est différente de . (vide) ou qu'on n'est pas sur la dernière colonne, on continue
        if (canMoveEast(grid, robotPosition)) {
            System.out.println("Can move to east");
            moveEast(grid, robotPosition);
            robotPosition.moveToEast();
        } else {
            System.out.println("Cannot move to north");
        }
    }

    private static void attemptMoveToSouth(List<List<String>> grid, Coord robotPosition) {
        // tant que la case au sud est différente de . (vide) ou qu'on n'est pas sur la dernière ligne, on continue
        if (canMoveSouth(grid, robotPosition)) {
            System.out.println("Can move to South");
            moveSouth(grid, robotPosition);
            robotPosition.moveToSouth();
        } else {
            System.out.println("Cannot move to South");
        }
    }

    private static void attemptMoveToWest(List<List<String>> grid, Coord robotPosition) {
        // tant que la case à l'ouest est différente de . (vide) ou qu'on n'est pas sur la première colonne, on continue
        if (canMoveWest(grid, robotPosition)) {
            System.out.println("Can move to west");
            moveWest(grid, robotPosition);
            robotPosition.moveToWest();
        } else {
            System.out.println("Cannot move to west");
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
                    .map(line -> Arrays.stream(line.split(""))
                                       .flatMap(Day15_2::wider)
                                       .collect(Collectors.toList()))
                    .toList();
    }

    private static List<String> initMoveAttempts(List<String> lines) {
        return lines.stream()
                    .dropWhile(line -> line.startsWith("#"))
                    .skip(1)
                    .flatMap(line -> Arrays.stream(line.split("")))
                    .toList();
    }

    private static Stream<String> wider(String in) {
        return switch (in) {
            case "#" -> Stream.of("#", "#");
            case "O" -> Stream.of("[", "]");
            case "." -> Stream.of(".", ".");
            case "@" -> Stream.of("@", ".");
            default -> throw new IllegalArgumentException("Unknown input character");
        };
    }

    private static boolean canMove(List<List<String>> grid, Coord robotCoord, Direction dir) {
        return switch (dir) {
            case N -> canMoveNorth(grid, robotCoord);
            case E -> canMoveEast(grid, robotCoord);
            case S -> canMoveSouth(grid, robotCoord);
            case W -> canMoveWest(grid, robotCoord);
        };
    }

    private static boolean canMoveNorth(List<List<String>> grid, Coord robotCoord) {
        if (robotCoord.getY() == 0) {
            return false;
        }

        // cannot move if the cell in front of the robot is a wall
        if (robotCoord.getY() > 0 && grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals("#")) {
            return false;
        }

        // can move if the cell in front of the robot is free
        if (robotCoord.getY() > 0 && grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals(".")) {
            return true;
        }

        // can move if the cell in front of the robot is a box and it can move
        if (grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals("[")) {
            return boxCanMoveNorth(grid, new Coord(robotCoord.getX(), robotCoord.getY() - 1));
        }
        if (grid.get(robotCoord.getY() - 1).get(robotCoord.getX() - 1).equals("[")) {
            return boxCanMoveNorth(grid, new Coord(robotCoord.getX() - 1, robotCoord.getY() - 1));
        }
        return false;
    }

    private static boolean boxCanMoveNorth(List<List<String>> grid, Coord boxCoord) {
        if (boxCoord.getY() == 0) {
            return false;
        }
        // cannot move if one of the cells in front of the box is a wall
        if (boxCoord.getY() > 0 && grid.get(boxCoord.getY() - 1).subList(boxCoord.getX(), boxCoord.getX() + 2).contains("#")) {
            return false;
        }

        // can move if all the cells in front of the box are free
        if (boxCoord.getY() > 0 && String.join("", grid.get(boxCoord.getY() - 1).subList(boxCoord.getX(), boxCoord.getX() + 2)).equals("..")) {
            return true;
        }

        // can move if all the boxes in front of the robot can move
        // locate the coordinates of the potential boxes in front of the box
        List<Coord> boxToCheck = new ArrayList<>();
        if (boxCoord.getX() > 1 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX() - 1).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX() - 1, boxCoord.getY() - 1));
        }
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX()).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX(), boxCoord.getY() - 1));
        }
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX() + 1).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX() + 1, boxCoord.getY() - 1));
        }
        if (!boxToCheck.isEmpty()) {
            return boxToCheck.stream().allMatch(coord -> boxCanMoveNorth(grid, coord));
        }
        return true;
    }

    private static boolean canMoveEast(List<List<String>> grid, Coord robotCoord) {
        if (robotCoord.getX() == grid.getFirst().size() - 3) {
            return false;
        }

        // cannot move if the cell in front of the robot is a wall
        if (robotCoord.getX() < grid.getFirst().size() - 3 && grid.get(robotCoord.getY()).get(robotCoord.getX() + 1).equals("#")) {
            return false;
        }

        // can move if the cell in front of the robot is free
        if (robotCoord.getX() < grid.getFirst().size() - 3 && grid.get(robotCoord.getY()).get(robotCoord.getX() + 1).equals(".")) {
            return true;
        }

        // can move if the cell in front of the robot is a box and it can move
        return boxCanMoveEast(grid, new Coord(robotCoord.getX() + 1, robotCoord.getY()));
    }

    private static boolean boxCanMoveEast(List<List<String>> grid, Coord boxCoord) {
        if (boxCoord.getX() == grid.getFirst().size() - 3) {
            return false;
        }
        // cannot move if the cell in front of the box is a wall
        if (boxCoord.getX() < grid.getFirst().size() - 3 && grid.get(boxCoord.getY()).get(boxCoord.getX() + 2).equals("#")) {
            return false;
        }

        // can move if the cell in front of the box is free
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY()).get(boxCoord.getX() + 2).equals(".")) {
            return true;
        }

        // can move if the box in front of the robot can move
        if (boxCoord.getX() < grid.getFirst().size() - 5 && grid.get(boxCoord.getY()).get(boxCoord.getX() + 2).equals("[")) {
            return boxCanMoveEast(grid, new Coord(boxCoord.getX() + 2, boxCoord.getY()));
        }

        return true;
    }

    private static boolean canMoveSouth(List<List<String>> grid, Coord robotCoord) {
        if (robotCoord.getY() == grid.size() - 1) {
            return false;
        }

        // cannot move if the cell in front of the robot is a wall
        if (robotCoord.getY() < grid.size() - 1 && grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals("#")) {
            return false;
        }

        // can move if the cell in front of the robot is free
        if (robotCoord.getY() < grid.size() - 1 && grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals(".")) {
            return true;
        }

        // can move if the cell in front of the robot is a box and it can move
        if (grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals("[")) {
            return boxCanMoveSouth(grid, new Coord(robotCoord.getX(), robotCoord.getY() + 1));
        }
        if (grid.get(robotCoord.getY() + 1).get(robotCoord.getX() - 1).equals("[")) {
            return boxCanMoveSouth(grid, new Coord(robotCoord.getX() - 1, robotCoord.getY() + 1));
        }
        return false;
    }

    private static boolean boxCanMoveSouth(List<List<String>> grid, Coord boxCoord) {
        if (boxCoord.getY() == grid.size() - 1) {
            return false;
        }
        // cannot move if one of the cells in front of the box is a wall
        if (boxCoord.getY() < grid.size() - 1 && grid.get(boxCoord.getY() + 1).subList(boxCoord.getX(), boxCoord.getX() + 2).contains("#")) {
            return false;
        }

        // can move if all the cells in front of the box are free
        if (boxCoord.getY() < grid.size() - 1 && String.join("", grid.get(boxCoord.getY() + 1).subList(boxCoord.getX(), boxCoord.getX() + 2)).equals("..")) {
            return true;
        }

        // can move if all the boxes in front of the robot can move
        // locate the coordinates of the potential boxes in front of the box
        List<Coord> boxToCheck = new ArrayList<>();
        if (boxCoord.getX() > 1 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX() - 1).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX() - 1, boxCoord.getY() + 1));
        }
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX()).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX(), boxCoord.getY() + 1));
        }
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX() + 1).equals("[")) {
            boxToCheck.add(new Coord(boxCoord.getX() + 1, boxCoord.getY() + 1));
        }
        if (!boxToCheck.isEmpty()) {
            return boxToCheck.stream().allMatch(coord -> boxCanMoveSouth(grid, coord));
        }
        return true;
    }

    private static boolean canMoveWest(List<List<String>> grid, Coord robotCoord) {
        if (robotCoord.getX() == 0) {
            return false;
        }

        // cannot move if the cell in front of the robot is a wall
        if (robotCoord.getX() > 0 && grid.get(robotCoord.getY()).get(robotCoord.getX() - 1).equals("#")) {
            return false;
        }

        // can move if the cell in front of the robot is free
        if (robotCoord.getX() > 0 && grid.get(robotCoord.getY()).get(robotCoord.getX() - 1).equals(".")) {
            return true;
        }

        // can move if the cell in front of the robot is a box and it can move
        return boxCanMoveWest(grid, new Coord(robotCoord.getX() - 2, robotCoord.getY()));
    }

    private static boolean boxCanMoveWest(List<List<String>> grid, Coord boxCoord) {
        if (boxCoord.getX() == 0) {
            return false;
        }
        // cannot move if the cell in front of the box is a wall
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY()).get(boxCoord.getX() - 1).equals("#")) {
            return false;
        }

        // can move if the cell in front of the box is free
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY()).get(boxCoord.getX() - 1).equals(".")) {
            return true;
        }

        // can move if the box in front of the robot can move
        if (boxCoord.getX() > 2 && grid.get(boxCoord.getY()).get(boxCoord.getX() - 2).equals("[")) {
            return boxCanMoveWest(grid, new Coord(boxCoord.getX() - 2, boxCoord.getY()));
        }

        return true;
    }

    private static void moveNorth(List<List<String>> grid, Coord robotCoord) {
        // can move if the cell in front of the robot is free
        if (robotCoord.getY() > 0 && grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals(".")) {
            grid.get(robotCoord.getY() - 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
            return;
        }

        // can move if the cell in front of the robot is a box and it can move
        if (robotCoord.getY() > 0 && grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals("[")) {
            moveBoxNorth(grid, new Coord(robotCoord.getX(), robotCoord.getY() - 1));
            grid.get(robotCoord.getY() - 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
        if (robotCoord.getY() > 0 && grid.get(robotCoord.getY() - 1).get(robotCoord.getX()).equals("]")) {
            moveBoxNorth(grid, new Coord(robotCoord.getX() - 1, robotCoord.getY() - 1));
            grid.get(robotCoord.getY() - 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
    }

    private static void moveBoxNorth(List<List<String>> grid, Coord boxCoord) {
        // locate the coordinates of the potential boxes in front of the box
        List<Coord> boxToMove = new ArrayList<>();
        if (boxCoord.getX() > 1 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX() - 1).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX() - 1, boxCoord.getY() - 1));
        }
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX()).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX(), boxCoord.getY() - 1));
        }
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY() - 1).get(boxCoord.getX() + 1).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX() + 1, boxCoord.getY() - 1));
        }
        boxToMove.forEach(coord -> moveBoxNorth(grid, coord));

        // can move if all the cells in front of the box are free
        if (boxCoord.getY() > 0 && String.join("", grid.get(boxCoord.getY() - 1).subList(boxCoord.getX(), boxCoord.getX() + 2)).equals("..")) {
            grid.get(boxCoord.getY() - 1).set(boxCoord.getX(), "[");
            grid.get(boxCoord.getY() - 1).set(boxCoord.getX() + 1, "]");
            grid.get(boxCoord.getY()).set(boxCoord.getX(), ".");
            grid.get(boxCoord.getY()).set(boxCoord.getX() + 1, ".");
        }

    }

    private static void moveEast(List<List<String>> grid, Coord robotCoord) {
        // can move if the cell in front of the robot is free
        if (robotCoord.getX() < grid.getFirst().size() - 1 && grid.get(robotCoord.getY()).get(robotCoord.getX() + 1).equals(".")) {
            grid.get(robotCoord.getY()).set(robotCoord.getX() + 1, "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }

        // can move if the cell in front of the robot is a box and it can move
        if (robotCoord.getY() < grid.getFirst().size() - 3 && grid.get(robotCoord.getY()).get(robotCoord.getX() + 1).equals("[")) {
            moveBoxEast(grid, new Coord(robotCoord.getX() + 1, robotCoord.getY()));
            grid.get(robotCoord.getY()).set(robotCoord.getX() + 1, "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
    }

    private static void moveBoxEast(List<List<String>> grid, Coord boxCoord) {
        // can move if the box in front of the robot can move
        if (boxCoord.getX() < grid.getFirst().size() - 5 && grid.get(boxCoord.getY()).get(boxCoord.getX() + 2).equals("[")) {
            moveBoxEast(grid, new Coord(boxCoord.getX() + 2, boxCoord.getY()));
        }

        // can move if the cell in front of the box is free
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY()).get(boxCoord.getX() + 2).equals(".")) {
            grid.get(boxCoord.getY()).set(boxCoord.getX() + 1, "[");
            grid.get(boxCoord.getY()).set(boxCoord.getX() + 2, "]");
            grid.get(boxCoord.getY()).set(boxCoord.getX(), ".");
            return;
        }
    }

    private static void moveSouth(List<List<String>> grid, Coord robotCoord) {
        // can move if the cell in front of the robot is free
        if (robotCoord.getY() < grid.size() - 1 && grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals(".")) {
            grid.get(robotCoord.getY() + 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
            return;
        }

        // can move if the cell in front of the robot is a box and it can move
        if (robotCoord.getY() < grid.size() - 1 && grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals("[")) {
            moveBoxSouth(grid, new Coord(robotCoord.getX(), robotCoord.getY() + 1));
            grid.get(robotCoord.getY() + 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
        if (robotCoord.getY() < grid.size() - 1 && grid.get(robotCoord.getY() + 1).get(robotCoord.getX()).equals("]")) {
            moveBoxSouth(grid, new Coord(robotCoord.getX() - 1, robotCoord.getY() + 1));
            grid.get(robotCoord.getY() + 1).set(robotCoord.getX(), "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
    }

    private static void moveBoxSouth(List<List<String>> grid, Coord boxCoord) {
        // locate the coordinates of the potential boxes in front of the box
        List<Coord> boxToMove = new ArrayList<>();
        if (boxCoord.getX() > 1 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX() - 1).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX() - 1, boxCoord.getY() + 1));
        }
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX()).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX(), boxCoord.getY() + 1));
        }
        if (boxCoord.getX() < grid.getFirst().size() - 4 && grid.get(boxCoord.getY() + 1).get(boxCoord.getX() + 1).equals("[")) {
            boxToMove.add(new Coord(boxCoord.getX() + 1, boxCoord.getY() + 1));
        }
        boxToMove.forEach(coord -> moveBoxSouth(grid, coord));

        // can move if all the cells in front of the box are free
        if (boxCoord.getY() < grid.size() - 1 && String.join("", grid.get(boxCoord.getY() + 1).subList(boxCoord.getX(), boxCoord.getX() + 2)).equals("..")) {
            grid.get(boxCoord.getY() + 1).set(boxCoord.getX(), "[");
            grid.get(boxCoord.getY() + 1).set(boxCoord.getX() + 1, "]");
            grid.get(boxCoord.getY()).set(boxCoord.getX(), ".");
            grid.get(boxCoord.getY()).set(boxCoord.getX() + 1, ".");
        }
    }

    private static void moveWest(List<List<String>> grid, Coord robotCoord) {
        // can move if the cell in front of the robot is free
        if (robotCoord.getX() > 0 && grid.get(robotCoord.getY()).get(robotCoord.getX() - 1).equals(".")) {
            grid.get(robotCoord.getY()).set(robotCoord.getX() - 1, "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }

        // can move if the cell in front of the robot is a box and it can move
        if (robotCoord.getX() > 2 && grid.get(robotCoord.getY()).get(robotCoord.getX() - 2).equals("[")) {
            moveBoxWest(grid, new Coord(robotCoord.getX() - 2, robotCoord.getY()));
            grid.get(robotCoord.getY()).set(robotCoord.getX() - 1, "@");
            grid.get(robotCoord.getY()).set(robotCoord.getX(), ".");
        }
    }

    private static void moveBoxWest(List<List<String>> grid, Coord boxCoord) {
        // can move if the box in front of the robot can move
        if (boxCoord.getX() > 4 && grid.get(boxCoord.getY()).get(boxCoord.getX() - 2).equals("[")) {
            moveBoxWest(grid, new Coord(boxCoord.getX() - 2, boxCoord.getY()));
        }

        // can move if the cell in front of the box is free
        if (boxCoord.getX() > 0 && grid.get(boxCoord.getY()).get(boxCoord.getX() - 1).equals(".")) {
            grid.get(boxCoord.getY()).set(boxCoord.getX() - 1, "[");
            grid.get(boxCoord.getY()).set(boxCoord.getX(), "]");
            grid.get(boxCoord.getY()).set(boxCoord.getX() + 1, ".");
        }
    }
}
