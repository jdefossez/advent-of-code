package com.jdefossez.adventofcode.year2024.days.day11;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day11 {

    static int pathCount = 0;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day11.class.getClassLoader().getResource("input/2024/day_11_test.txt")).toURI());

        List<Stone> stoneList = Files.lines(path)
                                     .map(line -> line.split(" "))
                                     .map(array -> Arrays.stream(array).map(Stone::new).collect(Collectors.toList()))
                                     .toList()
                                     .getFirst();

//        Map<String, Long> countByNumber = new HashMap();
//        Set<Long> allValues = new HashSet<>();

        System.out.println(stoneList);

        for (int j = 0; j < 6; j++) {
            System.out.println("Blink n° " + j);
            for (int i = stoneList.size() - 1; i >= 0; i--) {
                List<Stone> newStones = change(stoneList.get(i));
                stoneList.remove(i);
                stoneList.addAll(i, newStones);
//                allValues.addAll(newStones.stream().map(s -> s.number).map(Long::parseLong).toList());
            }
            // System.out.println(stoneList);
            System.out.println("AllValues : " + stoneList.size());
            System.out.println("AllValues : " + stoneList);
        }


//        System.out.println(stoneList);
        System.out.printf("Result %s", stoneList.size());
    }

    private static List<Stone> change(Stone stone) {
        if (stone.number.equals("0")) {
            return List.of(new Stone("1"));
        }
        if (stone.number.length() % 2 == 0) {
            return List.of(new Stone(stone.number.substring(0, stone.number.length() / 2)), new Stone(String.valueOf(Long.parseLong(stone.number.substring(stone.number.length() / 2)))));
        }
        return List.of(new Stone(String.valueOf(Long.parseLong(stone.number) * 2024)));
    }

    private record Stone(String number) {

    }
}
