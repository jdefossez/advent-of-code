package com.jdefossez.adventofcode.year2024.days.day05;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day05 {

    IntegerRuleComparator comparator;

    public static void main(String[] args) throws IOException, URISyntaxException {


        Path path = Paths.get(Objects.requireNonNull(Day05.class.getClassLoader().getResource("input/2024/day_05.txt")).toURI());

        String[] data =
                Files.lines(path)
                     .peek(System.out::println)
                     .toArray(String[]::new);

        List<Rule> rules = Arrays.stream(data).takeWhile(s -> !s.isEmpty())
                                 .map(rule -> rule.split("\\|"))
                                 .map(ab -> new Rule(Integer.parseInt(ab[0]), Integer.parseInt(ab[1])))
                                 .toList();

        List<List<Integer>> updates = Arrays.stream(data).dropWhile(s -> !s.isEmpty()).skip(1)
                                            .map(update -> Arrays.stream(update.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                                            .toList();

        rules.forEach(System.out::println);
        updates.forEach(System.out::println);

        updates.forEach(update -> {
            rules.forEach(rule -> {
                System.out.printf("%s is valid according rule %s : %s\n", update, rule, isValid(rule, update));
            });
            System.out.println();
        });

        List<List<Integer>> validUpdates = updates.stream().filter(update -> rules.stream().allMatch(rule -> isValid(rule, update))).toList();
        List<List<Integer>> invalidUpdates = updates.stream().filter(update -> rules.stream().anyMatch(rule -> !isValid(rule, update))).toList();

        System.out.println("Valides : ");
        validUpdates.forEach(System.out::println);

        System.out.println("Invalides : ");
        invalidUpdates.forEach(System.out::println);

        System.out.println();

        invalidUpdates.forEach(update -> {
            update.sort(new IntegerRuleComparator(rules));
            System.out.printf("%s \n", update);
        });

        var res2 = invalidUpdates.stream().mapToInt(update -> update.get(update.size() / 2)).sum();

        System.out.println("Result 2: " + res2);
    }

    private static boolean isValid(Rule rule, List<Integer> update) {
        for (int i = 0; i < update.size() - 1; i++) {
            if (update.get(i) == rule.b()) {
                for (int j = i + 1; j < update.size(); j++) {
                    if (update.get(j) == rule.a()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


//    private static void sort(Rule rule, List<Integer> update) {
//        var aIndex = update.indexOf(rule.a());
//        var bIndex = update.indexOf(rule.b());
//        System.out.printf("%s, rule: %s %s %s\n", update, rule, aIndex, bIndex);
//        if (bIndex > 0 && aIndex > bIndex) {
//            update.set(aIndex, rule.b());
//            update.set(bIndex, rule.a());
//            System.out.println(update);
//        }
//    }


}
