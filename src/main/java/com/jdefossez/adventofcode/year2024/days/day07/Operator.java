package com.jdefossez.adventofcode.year2024.days.day07;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public enum Operator {
    ADD(Long::sum),
    MUL((Long a, Long b) -> a * b),
    CONCAT((Long a, Long b) -> Long.parseLong(String.format("%s%s", a, b)));

    final BiFunction<Long, Long, Long> function;

    Operator(BiFunction<Long, Long, Long> function) {
        this.function = function;
    }

    public static List<List<Operator>> combinations(int operatorsLocationsCount) {
        var operatorsCount = Operator.values().length;
        var totalCount = Math.pow(operatorsCount, operatorsLocationsCount);

        List<List<Operator>> combinations = new ArrayList<>();
        for (int i = 0; i < totalCount; i++) {
            List<Operator> combination = new ArrayList<>();
            for (int j = 0; j < operatorsLocationsCount; j++) {
                combination.add(Operator.values()[((int) (i / Math.pow(operatorsCount, j))) % operatorsCount]);
            }
            combinations.add(combination);
        }

        return combinations;
    }

    public static void main(String[] args) {
        combinations(3).forEach(System.out::println);
    }
}
