package com.jdefossez.adventofcode.year2015.days;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {


    public static void main(String[] args) throws IOException {

        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2015/08.txt"), StandardCharsets.UTF_8);

        System.out.println("Part 1: " + part1(input)); // moins de 5118
//        System.out.println("Part 2: " + part2(input));

    }

    public static int part1(List<String> input) {
        return input.stream().mapToInt(s -> interpretEscapeSequences(s.substring(1, s.length() - 1))).sum();
    }

    public static int interpretEscapeSequences(String input) {
        int initialLength = input.length() + 2;
        System.out.print(input + " : " + initialLength + " ");

        int l = 0;
        while (!input.isEmpty()) {
            Pattern patternUnicode = Pattern.compile("^\\\\x([0-9A-Fa-f]{2})");
            Matcher matcherUnicode = patternUnicode.matcher(input);

            Pattern patternEscape = Pattern.compile("^\\\\.+");
            Matcher matcherEscape = patternEscape.matcher(input);


            if (matcherUnicode.find()) {
                input = input.substring(4);
            } else if (matcherEscape.find()) {
                input = input.substring(2);
            } else {
                input = input.substring(1);
            }
            l++;
        }
        System.out.println(l + " => " + (initialLength - l));
        return initialLength - l;
    }


//    public static int part2(List<String> input) {
//
//        int part1Result = part1(input);
//
//        Map<String, Wire> states = initStates(input);
//        states.get("b").setInput(String.valueOf(part1Result));
//
//        Wire targetWire = states.get("a");
//        targetWire.resolve(states);
//
//        return targetWire.getValue();
//    }

}
