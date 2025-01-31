package com.jdefossez.adventofcode.year2024.days.day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Day19 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day19.class.getClassLoader().getResource("input/2024/day_19.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        String[] tokens = lines.getFirst().split(", ");

        List<String> designs = lines.subList(2, lines.size());

        System.out.println("Part 1 : " + part1(tokens, designs));

        System.out.println("Part 2 : " + part2(tokens, designs));
    }

    private static int part1(String[] tokens, List<String> designs) {

        String patternBuilder = "^(" + String.join("|", tokens) + ")*$";

        Pattern pattern = Pattern.compile(patternBuilder);

        return (int) designs.stream().filter(design -> pattern.matcher(design).matches()).count();
    }

    private static long part2(String[] tokens, List<String> designs) {
        return designs.stream().mapToLong(design -> countCombinations(design, tokens)).sum();
    }

    private static long countCombinations(String design, String[] tokens) {
        long[] subStringCount = new long[design.length() + 1];

        subStringCount[0] = 1;

        for (int i = 1; i <= design.length(); i++) {
            for (String token : tokens) {
                if (token.length() <= i && design.startsWith(token, i - token.length())) {
                    subStringCount[i] += subStringCount[i - token.length()];
                }
            }
        }
        return subStringCount[design.length()];
    }

}
