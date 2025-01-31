package com.jdefossez.adventofcode.year2024.days.day06;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day06_2 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Map<Coord, Set<Dir>> fullPath = new HashMap<>();

        Path path = Paths.get(Objects.requireNonNull(Day06_2.class.getClassLoader().getResource("input/2024/day_06.txt")).toURI());

        List<List<String>> grid =
                Files.lines(path)
                     .map(line -> line.split(""))
                     .map(Arrays::asList)
                     .toList();

        State initialState = getInitialState(grid);
        State nextState = new State(initialState.getCoord(), initialState.getDir()); // used to not place a block at this location

        // init de la liste des coordonnées à tester
        try {
            while (nextState != null) {
                pushNextState(fullPath, nextState.getCoord(), nextState.getDir());
                nextState = move(nextState, grid);
            }
        } catch (RuntimeException e) {
//            log.warn("Cycle detected - Init phase, should not see me");
        }

        var result = fullPath.keySet()
                             .stream()
                             .filter(coord -> !coord.equals(initialState.getCoord()))
                             .mapToInt(coord -> {
                                 return simulateWithBlockAt(coord, grid, initialState);
                             })
                             .sum();

        System.out.printf("Result : %s\n", result);
    }

    private static int simulateWithBlockAt(Coord coord, List<List<String>> grid, State initialState) {

        Map<Coord, Set<Dir>> simulatedFullPath = new HashMap<>();
        grid.get(coord.y()).set(coord.x(), "O");

        State nextState = new State(initialState.getCoord(), initialState.getDir());
        try {
            while (nextState != null) {
                pushNextState(simulatedFullPath, nextState.getCoord(), nextState.getDir());
                nextState = move(nextState, grid);
            }
        } catch (RuntimeException e) {
            grid.get(coord.y()).set(coord.x(), ".");
            return 1;
        }
        grid.get(coord.y()).set(coord.x(), ".");
        return 0;
    }

    private static void pushNextState(Map<Coord, Set<Dir>> fullPath, Coord coord, Dir dir) {
        if (!fullPath.containsKey(coord)) {
            fullPath.put(coord, new HashSet<>());
        }
        if (!fullPath.get(coord).add(dir)) {
            throw new RuntimeException("Cycle detected");
        }
    }

    private static Coord nextCoord(State state, int rowMax, int colMax) {
        return switch (state.getDir()) {
            case N -> state.getCoord().y() > 0 ? new Coord(state.getCoord().x(), state.getCoord().y() - 1) : null;
            case E -> state.getCoord().x() < colMax ? new Coord(state.getCoord().x() + 1, state.getCoord().y()) : null;
            case S -> state.getCoord().y() < rowMax ? new Coord(state.getCoord().x(), state.getCoord().y() + 1) : null;
            case W -> state.getCoord().x() > 0 ? new Coord(state.getCoord().x() - 1, state.getCoord().y()) : null;
        };
    }

    private static State move(State currentState, List<List<String>> grid) {
        Coord nextCoord = nextCoord(currentState, grid.size() - 1, grid.getFirst().size() - 1);
        if (nextCoord == null) {
            return null;
        }
        String nextChar = grid.get(nextCoord.y()).get(nextCoord.x());

        return switch (nextChar) {
            case "." -> new State(nextCoord, currentState.getDir());
            case "#", "O" -> {
                Dir rightDir = currentState.getDir().turnRight();
                currentState.setDir(rightDir);
                yield move(currentState, grid);
            }
            case "-", "|" -> new State(nextCoord, currentState.getDir());
            case "+", "^", ">", "v", "<" -> new State(nextCoord, currentState.getDir());
            default -> throw new IllegalStateException("Unexpected value: " + nextChar);
        };

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


}
