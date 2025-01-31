package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day07 {

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2015/day07"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {

        Map<String, Wire> states = initStates(input);

        Wire targetWire = states.get("a");
        targetWire.resolve(states);

        return targetWire.getValue();
    }


    public static int part2(List<String> input) {

        int part1Result = part1(input);

        Map<String, Wire> states = initStates(input);
        states.get("b").setInput(String.valueOf(part1Result));

        Wire targetWire = states.get("a");
        targetWire.resolve(states);

        return targetWire.getValue();
    }

    private static Map<String, Wire> initStates(List<String> input) {
        Map<String, Wire> states = new HashMap<>();

        for (String line : input) {
            String[] parts = line.split(" -> ");
            String wireName = parts[1];
            Wire wire = new Wire(wireName, parts[0]);
            states.put(wireName, wire);
        }
        return states;
    }
}
