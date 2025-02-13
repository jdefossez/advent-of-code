package com.jdefossez.adventofcode.year2018;

import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2018/03.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static long part1(List<String> input) {
        List<Claim> claims = input.stream().map(Claim::parse).toList();

        int[][] fabric = new int[1000][1000];

        for (Claim claim : claims) {
            for (int x = claim.x; x < claim.x + claim.width; x++) {
                for (int y = claim.y; y < claim.y + claim.height; y++) {
                    fabric[x][y] = fabric[x][y] + 1;
                }
            }
        }

        int count = 0;
        for (int x = 0; x < fabric.length; x++) {
            for (int y = 0; y < fabric[x].length; y++) {
                if (fabric[x][y] > 1) {
                    count++;
                }
            }
        }

        return count;
    }

    public static String part2(List<String> input) {

        List<Claim> claims = input.stream().map(Claim::parse).toList();

        int[][] fabric = new int[1000][1000];

        for (Claim claim : claims) {
            for (int x = claim.x; x < claim.x + claim.width; x++) {
                for (int y = claim.y; y < claim.y + claim.height; y++) {
                    fabric[x][y] = fabric[x][y] + 1;
                }
            }
        }

        for (Claim claim : claims) {
            if (!overlapped(claim, fabric)) {
                return claim.id;
            }
        }

        return "";
    }

    private static boolean overlapped(Claim claim, int[][] fabric) {
        for (int x = claim.x; x < claim.x + claim.width; x++) {
            for (int y = claim.y; y < claim.y + claim.height; y++) {
                if (fabric[x][y] > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @AllArgsConstructor
    static class Claim {

        static final Pattern pattern = Pattern.compile("^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$");

        String id;
        int x;
        int y;
        int width;
        int height;

        static Claim parse(String input) {
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                return new Claim(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
            }
            throw new InvalidInputDataException(input + " is not a valid Claim");
        }

        @Override
        public String toString() {
            return String.format("%s @ %d,%d: %dx%d", id, x, y, width, height);
        }
    }

}
