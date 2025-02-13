package com.jdefossez.adventofcode.year2017;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day07 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2017/07.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static String part1(List<String> input) {

        Map<String, Program> allPrograms = new HashMap<>();
        Set<String> holdPrograms = new HashSet<>();

        for (String s : input) {
            Pattern pattern = Pattern.compile("^(\\w+) \\((\\d+)\\)(?: -> (.*))?$");
            Matcher m = pattern.matcher(s);

            if (m.find()) {

                String name = m.group(1);
                Integer weight = Integer.parseInt(m.group(2));
                List<String> localHoldPrograms = List.of();
                if (m.group(3) != null) {
                    localHoldPrograms = Arrays.stream(m.group(3).split(",\\s+")).toList();
                }

                holdPrograms.addAll(localHoldPrograms);

                Program p = new Program(name, weight, localHoldPrograms, weight, false);

                if (!allPrograms.containsKey(name)) {
                    allPrograms.put(name, p);
                }
            }
        }

        allPrograms.keySet().removeAll(holdPrograms);

        return allPrograms.keySet().stream().toList().getFirst();
    }


    public static String part2(List<String> input) {

        // TODO pas fini. La réponse est 1674, il faut trouver la tour, ayant ses propres holdPrograms équilibrées, mais dont le poids total est différent de celui de ses voisins. énoncé pas clair !
        Map<String, Program> allPrograms = new HashMap<>();

        for (String s : input) {
            Pattern pattern = Pattern.compile("^(\\w+) \\((\\d+)\\)(?: -> (.*))?$");
            Matcher m = pattern.matcher(s);

            if (m.find()) {

                String name = m.group(1);
                Integer weight = Integer.parseInt(m.group(2));
                List<String> localHoldPrograms = List.of();
                if (m.group(3) != null) {
                    localHoldPrograms = Arrays.stream(m.group(3).split(",\\s+")).toList();
                }

                Program p = new Program(name, weight, localHoldPrograms, weight, false);

                if (!allPrograms.containsKey(name)) {
                    allPrograms.put(name, p);
                }
            }
        }

        calculateTotalWeight(allPrograms.get(part1(input)), allPrograms);

        for (Program program : allPrograms.values()) {
            System.out.print(program);
            System.out.print(" -> ");
            for (String holdProgram : program.holdPrograms) {
                System.out.print(allPrograms.get(holdProgram));
                System.out.print(", ");
            }
            System.out.println();
        }

        for (Program program : allPrograms.values()) {
            if (!program.holdPrograms.isEmpty()) {

                Map<Integer, List<Program>> holdProgramsGroupedByTotalWeight = program.holdPrograms.stream()
                                                                                                   .map(allPrograms::get)
                                                                                                   .collect(Collectors.groupingBy(p -> p.totalWeight));

                if (holdProgramsGroupedByTotalWeight.size() > 1) {
                    Program divergentProgram = holdProgramsGroupedByTotalWeight.values().stream()
                                                                               .filter(l -> l.size() == 1)
                                                                               .map(List::getFirst)
                                                                               .findFirst().get();

                    int otherProgramTotalWeight = holdProgramsGroupedByTotalWeight.values().stream()
                                                                                  .filter(l -> l.size() > 1)
                                                                                  .map(List::getFirst).map(p -> p.totalWeight)
                                                                                  .findFirst().get();

                    return String.valueOf(divergentProgram.weight - (divergentProgram.totalWeight - otherProgramTotalWeight)); // 1494 too low, 86766 too high

                }
            }
        }

        return "Not found";
    }

    private static int calculateTotalWeight(Program p, Map<String, Program> allPrograms) {
        if (p.holdPrograms.isEmpty()) {
            return p.weight;
        }
        if (p.totalWeightCalculated) {
            return p.totalWeight;
        }
        int sumHold = p.holdPrograms.stream().map(allPrograms::get).mapToInt(hp -> calculateTotalWeight(hp, allPrograms)).sum();
        p.totalWeight = p.weight + sumHold;
        p.totalWeightCalculated = true;
//        System.out.println(p);
        return p.totalWeight;
    }

    @AllArgsConstructor
    static class Program {
        String name;
        Integer weight;
        List<String> holdPrograms;
        Integer totalWeight;
        boolean totalWeightCalculated = false;

        @Override
        public String toString() {
            return String.format("%s: (%d), %d", name, weight, totalWeight);
        }
    }
}
