package com.jdefossez.adventofcode.year2024.days.day08;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day08 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day08.class.getClassLoader().getResource("input/2024/day_08.txt")).toURI());

        List<List<String>> grid =
                Files.lines(path)
                     .map(line -> line.split(""))
                     .map(Arrays::asList)
                     .toList();

//        grid.stream().map(l -> String.join("", l)).forEach(System.out::println);

        Map<String, List<Coord>> antennas = getAntennas(grid);
//        antennas.forEach((s, coords) -> System.out.printf("%s: %s\n", s, coords));

//        antennas.forEach((s, coords) -> System.out.printf("%s: %s\n", s, getSegments(coords)));

        Set<Coord> result = new HashSet<>();
        for (Map.Entry<String, List<Coord>> entry : antennas.entrySet()) {
//            System.out.println(entry);
//            if (entry.getKey().equals("9"))
            for (Segment segment : getSegments(entry.getValue())) {
//                result.addAll(getAntinodes(segment, grid.getFirst().size(), grid.size()));
                result.addAll(getAntinodesPart2(segment, grid.getFirst().size(), grid.size()));
//                System.out.println(segment + ":" + getAntinodes(segment, grid.getFirst().size(), grid.size()));
            }
        }

        System.out.printf("Result %s", result.size());

    }

    private static Map<String, List<Coord>> getAntennas(List<List<String>> grid) {
        Map<String, List<Coord>> result = new HashMap<>();
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).size(); col++) {
                String c = grid.get(row).get(col);
                if (c.matches("[a-zA-Z0-9]")) {
                    result.putIfAbsent(c, new ArrayList<>());
                    result.get(c).add(new Coord(col, row));
                }
            }
        }
        return result;
    }

    private static List<Segment> getSegments(List<Coord> coords) {
        List<Segment> result = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Coord a = coords.get(i);
            for (Coord b : coords.subList(i + 1, coords.size())) {
                result.add(new Segment(a, b));
            }
        }

        return result;
    }

    private static List<Coord> getAntinodes(Segment segment, int xMax, int yMax) {
        List<Coord> result = new ArrayList<>();

        int xA1 = segment.a().x() - segment.getXLength();
        int yA1 = segment.a().y() - segment.getYLength();
        if (xA1 >= 0 && xA1 < xMax && yA1 >= 0 && yA1 < yMax) {
            result.add(new Coord(xA1, yA1));
        }

        int xA2 = segment.b().x() + segment.getXLength();
        int yA2 = segment.b().y() + segment.getYLength();
        if (xA2 >= 0 && xA2 < xMax && yA2 >= 0 && yA2 < yMax) {
            result.add(new Coord(xA2, yA2));
        }

        return result;
    }

//    private static Set<Coord> getAntinodesPart2(Segment segment, int xMax, int yMax) {
//        Set<Coord> result = new HashSet<>();
//
//        float slope = (float) segment.getYLength() / segment.getXLength();
//        float yOrigin = segment.a().y() - slope * segment.a().x();
//
//        System.out.printf("%s: y=%f x + %f\n", segment, slope, yOrigin);
//
//        if (Math.abs(slope) > 0.5) {
//            for (int i = 0; i < xMax; i++) {
//                float y = slope * i + yOrigin;
//                System.out.printf("y:%f => %f %s\n", y, y - (int) y, isInteger(y));
//                if (isInteger(y) && y >= 0 && y < yMax) {
//                    var newCoord = new Coord(i, (int) y);
//                    System.out.printf("add new x coord %s\n", newCoord);
//                    result.add(newCoord);
//                }
//            }
//        } else {
//            for (int i = 0; i < yMax; i++) {
//                float x = (i - yOrigin) / slope;
//                System.out.printf("x:%f => %f %s\n", x, x - (int) x, isInteger(x));
//                if (isInteger(x) && x >= 0 && x < xMax) {
//                    var newCoord = new Coord((int) x, i);
//                    System.out.printf("add new y coord %s\n", newCoord);
//                    result.add(newCoord);
//                }
//            }
//        }
//
//
//        return result;
//    }

    private static Set<Coord> getAntinodesPart2(Segment segment, int xMax, int yMax) {
        Set<Coord> result = new HashSet<>();

        int gcd = gcd(segment.getYLength(), segment.getXLength());
        int deltaY = segment.getYLength() / gcd;
        int deltaX = segment.getXLength() / gcd;

        // + delta
        Coord coord = segment.a();
        while (coord.isValid(xMax, yMax)) {
            System.out.printf("+ Add %s\n", coord);
            result.add(coord);
            coord = new Coord(coord.x() + deltaX, coord.y() + deltaY);
        }

        // - delta
        coord = new Coord(segment.a().x() - deltaX, segment.a().y() - deltaY);
        while (coord.isValid(xMax, yMax)) {
            System.out.printf("- Add %s\n", coord);
            result.add(coord);
            coord = new Coord(coord.x() - deltaX, coord.y() - deltaY);
        }

        System.out.printf("%s: gcd=%d, deltaX = %d, deltaY = %d, result = %s\n", segment, gcd, deltaX, deltaY, result);

        return result;
    }

    private static boolean isInteger(float f) {
        int intPart = (int) f;
        var rest = f - intPart;
        return rest < 0.000001;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

}
