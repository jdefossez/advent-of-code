package com.jdefossez.adventofcode.year2016.days;

import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) throws IOException {

        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2016/04.txt"));

        Pattern p = Pattern.compile("^([a-z\\-]+)-(\\d+)\\[([a-z]+)]$");

        List<Room> rooms = input.stream().map(s -> {
                                    Matcher m = p.matcher(s);
                                    if (m.find()) {
                                        return new Room(m.group(1), Integer.parseInt(m.group(2)), m.group(3));
                                    }
                                    throw new InvalidInputDataException("the line " + s + " is not a valid triangle definition");
                                })
                                .toList();
        System.out.println("Part 1: " + part1(rooms));
        System.out.println("Part 2: " + part2(rooms));

    }

    public static int part1(List<Room> rooms) {
        return rooms.stream().filter(Room::isValid).mapToInt(Room::id).sum();
    }

    public static int part2(List<Room> triangles) {
        return triangles.stream()
                        .filter(r -> rotate(r.name(), r.id()).contains("northpole object storage"))
                        .map(Room::id)
                        .findFirst()
                        .orElseThrow();
    }

    private static String rotate(String s, int offset) {
        return s.chars()
                .mapToObj(c -> rotate(c, offset))
                .map(Object::toString)
                .collect(Collectors.joining()) + " " + offset;
    }

    private static char rotate(int c, int offset) {
        if (c == '-') {
            return ' ';
        }
        return (char) (97 + ((c - 97) + offset) % 26);
    }

    public record Room(String name, int id, String checksum) {

        boolean isValid() {

            var map = Arrays.stream(name.replace("-", "").trim().split(""))
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                            .entrySet()
                            .stream()
                            .sorted((entry1, entry2) -> {
                                int valueComparison = entry2.getValue().compareTo(entry1.getValue());
                                if (valueComparison != 0) {
                                    return valueComparison;  // Si les valeurs sont différentes, trier par valeur
                                } else {
                                    return entry1.getKey().compareTo(entry2.getKey());  // Sinon, trier par clé
                                }
                            });
//                            .sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue()));

            var list = map.limit(5).map(Map.Entry::getKey).collect(Collectors.joining());
//            System.out.println(list.equals(checksum));
            return list.equals(checksum);
        }
    }
}
