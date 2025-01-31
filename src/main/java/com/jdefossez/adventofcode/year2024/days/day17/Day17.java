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
import java.util.stream.Collectors;

public class Day17 {

    private static final Pattern REGISTER_PATTERN = Pattern.compile("^Register .: (\\d+)$");
    private static final Pattern PROGRAM_PATTERN = Pattern.compile("^Program: ([0-7,]+)$");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day17.class.getClassLoader().getResource("input/2024/day_17.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        // part 1: out for first execution
        Computer computer = initComputer(lines);
        System.out.println(computer.executeProgram());

        // part 2: min value of A to get the same output program than the input one
        String initialProgram = computer.getProgram().stream().map(Object::toString).collect(Collectors.joining(","));

        int numThreads = Runtime.getRuntime().availableProcessors();

        int rangePerThread = Integer.MAX_VALUE / numThreads;
//        numThreads = 1;

        System.out.println("Init of " + numThreads + " threads");
        // Create and start the threads
        for (int t = 0; t < numThreads; t++) {
            int start = t * rangePerThread;
            int end = (t + 1) * rangePerThread;

            // Handle the last chunk to include the remaining integers
            if (t == numThreads - 1) {
                end = Integer.MAX_VALUE;
            }

            System.out.println("Init thread " + t + " start: " + start + " end: " + end);
            Computer c = new Computer(0, 0, 0, computer.getProgram(), 0, new ArrayList<>(), "", computer.getProgramString());
            Thread thread = new Thread(new RangeProcessor(c, initialProgram, 190593310997519L, 190593310997520L));
            thread.start();
        }


//        for (int i = 100000000; i < Integer.MAX_VALUE; i++) {
//            computer.getOutputBuffer().clear();
//            computer.setA(i);
//
//            String result = computer.executeProgram();
//
////            System.out.println("*" + result + "*");
////            System.out.println("*" + initialProgram + "*");
//            if (result.equals(initialProgram)) {
//                System.out.println("Minimum A is : " + i);
//                break;
//            }
//            if (i % 1000000 == 0) {
//                System.out.println(i);
//            }
//        }
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

    static class RangeProcessor implements Runnable {
        private final Computer computer;
        private final String initialProgram;
        private final long start;
        private final long end;

        public RangeProcessor(Computer computer, String initialProgram, long start, long end) {
            this.computer = computer;
            this.initialProgram = initialProgram;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (long i = start; i < end; i++) {
                computer.setOutputBuffer("");
                computer.setA(i);

                String result = computer.executeProgram().substring(1);

                if (result.equals(initialProgram)) {
                    System.out.println("> * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * Minimum A is : " + i);
                    break;
                }
                if (i % 100000000 == 0) {
                    System.out.println(i);
                    System.out.println("result *" + result + "* initialProgram " + initialProgram);
                }
            }
        }
    }

}
