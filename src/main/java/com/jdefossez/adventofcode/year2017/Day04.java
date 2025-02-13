package com.jdefossez.adventofcode.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) throws IOException {

        List<String> passphraseList = Files.readAllLines(Path.of("src/main/resources/input/2017/04.txt"));

        System.out.println("Part 1: " + part1(passphraseList));
        System.out.println("Part 2: " + part2(passphraseList));

    }

    public static int part1(List<String> passphraseList) {

//        return (int) passphraseList.stream()
//                                   .map(p -> Arrays.stream(p.split("\s+"))
//                                                   .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                                   )
//                                   .map(m -> m.values().stream().mapToLong(l -> l).reduce((left, right) -> left * right))
//                                   .filter(optionalLong -> optionalLong.getAsLong() == 1)
//                                   .count();

        return (int) passphraseList.stream()
                                   .filter(passphrase -> Arrays.stream(passphrase.split("\\s+"))
                                                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                               .values()
                                                               .stream()
                                                               .allMatch(count -> count == 1))
                                   .count();
    }


    public static int part2(List<String> passphraseList) {
        return (int) passphraseList.stream()
                                   .filter(passphrase -> Arrays.stream(passphrase.split("\\s+"))
                                                               .map(w -> Arrays.stream(w.split("")).sorted().collect(Collectors.joining()))
                                                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                               .values()
                                                               .stream()
                                                               .allMatch(count -> count == 1))
                                   .count();
    }
}
