package com.jdefossez.adventofcode.year2024.days.day02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record Day02DataLine(List<Integer> report) {

    @Override
    public String toString() {
        return String.format("%s: is safe A : %s, is safe B : %s", report, isSafeA(), isSafeB());
    }

    private boolean isSafe(List<Integer> values) {
        if (values.size() == 1) {
            return true;
        }

        int direction = values.get(1) - values.get(0);
        // if direction = 0 then the report is invalid
        if (direction == 0) {
            return false;
        }

        // if direction is < 0 then the report should decrease
        if (direction < 0) {
            return IntStream.range(0, values.size() - 1)
                            .mapToObj(i -> {
                                var difference = values.get(i + 1) - values.get(i);
                                return difference < 0 && difference > -4;
                            })
                            .allMatch(b -> b == Boolean.TRUE)
                    ;
        }

        // if direction is > 0 then the report should increase
        return IntStream.range(0, values.size() - 1)
                        .mapToObj(i -> {
                            var difference = values.get(i + 1) - values.get(i);
                            return difference > 0 && difference < 4;
                        })
                        .allMatch(b -> b == Boolean.TRUE)
                ;
    }

    public boolean isSafeA() {
        return isSafe(report);
    }

    public boolean isSafeB() {
        return isSafe(report) ||
                IntStream.range(0, report().size())
                         .mapToObj(i -> {
                             var subList = new ArrayList<>(report);
                             subList.subList(i, i + 1).clear();
                             return subList;
                         }).peek(System.out::println)
                         .anyMatch(this::isSafe);
    }
}
