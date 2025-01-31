package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class Day02 {

    private final static Pattern DIMENSION_PATTERN = Pattern.compile("^(\\d+)x(\\d+)x(\\d+)$");

    public static void main(String[] args) throws IOException {

        // read file day02 into a String
        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2015/day02"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {
        int total = 0;
        for (String line : input) {
            var matcher = DIMENSION_PATTERN.matcher(line);
            if (matcher.matches()) {
                int l = Integer.parseInt(matcher.group(1));
                int w = Integer.parseInt(matcher.group(2));
                int h = Integer.parseInt(matcher.group(3));
                int lw = l * w;
                int wh = w * h;
                int hl = h * l;
                int min = Math.min(Math.min(lw, wh), hl);
                total += 2 * lw + 2 * wh + 2 * hl + min;
            }
        }
        return total;
    }

    public static int part2(List<String> input) {
        int total = 0;
        for (String line : input) {
            var matcher = DIMENSION_PATTERN.matcher(line);
            if (matcher.matches()) {
                int l = Integer.parseInt(matcher.group(1));
                int w = Integer.parseInt(matcher.group(2));
                int h = Integer.parseInt(matcher.group(3));
                // shortest side
                int min = Math.min(Math.min(l, w), h);
                // second-shortest side
                int secondMin = l + w + h - min - Math.max(Math.max(l, w), h);

                total += min * 2 + secondMin * 2 + l * w * h;
            }
        }
        return total;
    }

}
