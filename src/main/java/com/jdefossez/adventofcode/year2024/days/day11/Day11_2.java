package com.jdefossez.adventofcode.year2024.days.day11;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11_2 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day11_2.class.getClassLoader().getResource("input/2024/day_11.txt")).toURI());

        List<String> stoneList = Files.lines(path)
                                      .map(line -> line.split(" "))
                                      .map(array -> Arrays.stream(array).collect(Collectors.toList()))
                                      .toList()
                                      .getFirst();

        Map<String, Long> countByNumber = stoneList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(countByNumber);

        System.out.println(stoneList);

        for (int j = 0; j < 75; j++) {
            Map<String, Long> copyOfCountByNumber = new HashMap<>();
            for (Map.Entry<String, Long> entry : countByNumber.entrySet()) {
                Long countOfCurrent = entry.getValue();
                List<String> newStones = change(entry.getKey());
                newStones.forEach(number -> {
                    copyOfCountByNumber.putIfAbsent(number, 0L);
                    copyOfCountByNumber.put(number, copyOfCountByNumber.get(number) + countOfCurrent);
                });
            }
            countByNumber = copyOfCountByNumber;
        }

        Long result = countByNumber.values().stream().mapToLong(Long::valueOf).sum();

        System.out.printf("Result %s", result);
    }


    private static List<String> change(String number) {
        if (number.equals("0")) {
            return List.of("1");
        }
        if (number.length() % 2 == 0) {
            return List.of(number.substring(0, number.length() / 2), String.valueOf(Long.parseLong(number.substring(number.length() / 2))));
        }
        return List.of(String.valueOf(Long.parseLong(number) * 2024));
    }

}
