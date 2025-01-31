package com.jdefossez.adventofcode.year2024.days.day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17_2 {

    private static final Pattern REGISTER_PATTERN = Pattern.compile("^Register .: (\\d+)$");
    private static final Pattern PROGRAM_PATTERN = Pattern.compile("^Program: ([0-7,]+)$");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day17_2.class.getClassLoader().getResource("input/2024/day_17.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        // part 1: out for first execution
        Computer computer = initComputer(lines);
        System.out.println(computer.executeProgram());

        // part 2: min value of A to get the same output program than the input one
        Computer c = new Computer(0, 0, 0, computer.getProgram(), 0, new ArrayList<>(), "", computer.getProgramString());


        boolean found = false;
        long a = 0;
        int s = 1;
        while (!found) {
            c.setA(a);
            c.setPointer(0);
            c.setOutputBuffer("");
            c.getOutputBufferList().clear();

            c.executeProgram();

            if (c.getOutputBufferList().equals(c.getProgram())) {
                found = true;
                c.setA(a);
            } else if (c.getOutputBufferList().equals(c.getProgram().subList(c.getProgram().size() - s, c.getProgram().size()))) {
                a <<= 3;
                s++;
            } else {
                a++;
            }
        }

        System.out.println(c.getA());
    }

    private static Computer initComputer(List<String> lines) {

        Computer computer = new Computer();

        Matcher matcher = REGISTER_PATTERN.matcher(lines.getFirst());
        if (matcher.find()) {
            computer.setA(Integer.parseInt(matcher.group(1)));
        }

        matcher = REGISTER_PATTERN.matcher(lines.get(1));
        if (matcher.find()) {
            computer.setB(Integer.parseInt(matcher.group(1)));
        }

        matcher = REGISTER_PATTERN.matcher(lines.get(2));
        if (matcher.find()) {
            computer.setC(Integer.parseInt(matcher.group(1)));
        }

        matcher = PROGRAM_PATTERN.matcher(lines.get(4));
        if (matcher.find()) {
            computer.setProgram(Arrays.stream(matcher.group(1).split(","))
                                      .map(Integer::parseInt)
                                      .toList());
            computer.setProgramString(matcher.group(1).trim());
        }

        return computer;
    }
}
