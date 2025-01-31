package com.jdefossez.adventofcode.year2024.days.day06;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day06 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Map<Coord, Set<Dir>> fullPath = new HashMap<>();

        Path path = Paths.get(Objects.requireNonNull(Day06.class.getClassLoader().getResource("input/2024/day_06_test.txt")).toURI());

        List<List<String>> data =
                Files.lines(path)
                     .map(line -> line.split(""))
                     .map(Arrays::asList)
                     .toList();

        State currentState = getInitialState(data);

        fullPath.put(currentState.getCoord(), new HashSet<>());
        fullPath.get(currentState.getCoord()).add(currentState.getDir());

        int count = 1;

        var nextCoord = nextCoord(currentState, data);

        try {
            while (nextCoord != null) {
                String nextChar = data.get(nextCoord.y()).get(nextCoord.x());
                switch (nextChar) {
                    case "#", "O" -> {
                        data.get(currentState.getCoord().y()).set(currentState.getCoord().x(), "+");
                        addNextState(fullPath, currentState.getCoord(), currentState.getDir());
                        Dir rightDir = currentState.getDir().turnRight();
                        System.out.printf("rightDir %s", rightDir);
                        currentState.setDir(rightDir);
                        addNextState(fullPath, currentState.getCoord(), rightDir);
                    }
                    case "." -> {
                        System.out.println("case \".\"");
                        count++;
                        data.get(nextCoord.y()).set(nextCoord.x(), getNextPathChar(currentState.getDir()));
                        currentState.setCoord(nextCoord);
                        addNextState(fullPath, nextCoord, currentState.getDir());
                    }
                    case "-", "|" -> {
                        System.out.println("case \"-, |\"");
                        data.get(nextCoord.y()).set(nextCoord.x(), getNextPathChar(currentState.getDir(), nextChar));
                        currentState.setCoord(nextCoord);
                        addNextState(fullPath, nextCoord, currentState.getDir());
                    }
                    case "+", "^", ">", "v", "<" -> {
                        System.out.println("case \"+, ^, >, v, <\"");
                        currentState.setCoord(nextCoord);
                        addNextState(fullPath, nextCoord, currentState.getDir());
                    }
                }
                nextCoord = nextCoord(currentState, data);
            }
        } catch (RuntimeException e) {
            System.out.println("End of path. Cycle detected");
        }
        data.stream().map(list -> String.join("", list)).forEach(System.out::println);
        System.out.printf("Result : %s\n", count);
        System.out.printf("Full path: %s\n", fullPath);
    }

    private static void addNextState(Map<Coord, Set<Dir>> fullPath, Coord coord, Dir dir) {
        System.out.printf("addNextState : %s %s\n", coord, dir);
        if (!fullPath.containsKey(coord)) {
            fullPath.put(coord, new HashSet<>());
        }
        if (!fullPath.get(coord).add(dir)) {
            throw new RuntimeException("Cycle detected");
        }
    }


    private static String getNextPathChar(Dir dir) {
        if (dir == Dir.N || dir == Dir.S) {
            return "|";
        } else {
            return "-";
        }
    }

    private static String getNextPathChar(Dir dir, String existingChar) {
        if ((dir == Dir.N || dir == Dir.S)) {
            if (existingChar.equals("-")) {
                return "+";
            }
            return "|";
        } else {
            if (existingChar.equals("|")) {
                return "+";
            }
            return "-";
        }
    }

    private static State getInitialState(List<List<String>> data) {
        int xPos = 0;
        int yPos = 0;
        Dir dir = Dir.N;

        for (int row = 0; row < data.size(); row++) {
            for (int col = 0; col < data.get(row).size(); col++) {
                var c = data.get(row).get(col);
                switch (c) {
                    case "^" -> {
                        xPos = col;
                        yPos = row;
                        dir = Dir.N;
                    }
                    case ">" -> {
                        xPos = col;
                        yPos = row;
                        dir = Dir.E;
                    }
                    case "v" -> {
                        xPos = col;
                        yPos = row;
                        dir = Dir.S;
                    }
                    case "<" -> {
                        xPos = col;
                        yPos = row;
                        dir = Dir.W;
                    }
                }
            }
        }

        // initial state
        return new State(new Coord(xPos, yPos), dir);
    }

    private static Coord nextCoord(State state, List<List<String>> grid) {
        return nextCoord(state, grid.size() - 1, grid.getFirst().size() - 1);
    }

    private static Coord nextCoord(State state, int rowMax, int colMax) {
        return switch (state.getDir()) {
            case N -> state.getCoord().y() > 0 ? new Coord(state.getCoord().x(), state.getCoord().y() - 1) : null;
            case E -> state.getCoord().x() < colMax ? new Coord(state.getCoord().x() + 1, state.getCoord().y()) : null;
            case S -> state.getCoord().y() < rowMax ? new Coord(state.getCoord().x(), state.getCoord().y() + 1) : null;
            case W -> state.getCoord().x() > 0 ? new Coord(state.getCoord().x() - 1, state.getCoord().y()) : null;
        };
    }


}
