package com.jdefossez.adventofcode.year2024.days.day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {

    private static final int UNVISITED = -1;
    private static final Pattern BUTTON_PATTERN = Pattern.compile("X\\+(\\d+), Y\\+(\\d+)");
    private static final Pattern PRIZE_PATTERN = Pattern.compile("X=(\\d+), Y=(\\d+)");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day13.class.getClassLoader().getResource("input/2024/day_13.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<ClawMachine> machines = initClawMachines(lines);
//        machines.forEach(System.out::println);

        long tokenCount = machines.stream().map(Day13::solveClawMachine).flatMapToLong(list -> list.stream().mapToLong(Day13::tokenCount)).sum();

        System.out.println("Total token cost : " + tokenCount);

        // + de 1486058683724
    }

    private static List<Solution> solveClawMachine(ClawMachine m) {
//        System.out.println(m);
        long det = m.xOffsetA * m.yOffsetB - m.xOffsetB * m.yOffsetA;
//        System.out.printf("Det : %d\n", det);
        if (det != 0) {
            double x = (double) (m.yOffsetB * m.xPrize - m.xOffsetB * m.yPrize) / det;
            double y = (double) (-m.yOffsetA * m.xPrize + m.xOffsetA * m.yPrize) / det;
            if (x >= 0 && y >= 0 && x % 1 == 0 && y % 1 == 0) {
                boolean isValid = x * m.xOffsetA + y * m.xOffsetB == m.xPrize && x * m.yOffsetA + y * m.yOffsetB == m.yPrize;
//                if (!isValid) {
//                    System.out.println(m);
//                    System.out.println(det);
//                    System.out.printf("Solution a:%s b:%s - is ok %s\n", x, y, isValid);
//                }
                System.out.printf("Solution a:%s b:%s - is ok %s\n", x, y, isValid);
                return List.of(new Solution((long) x, (long) y));
            } else {
                System.out.println("No solution");
            }
        } else {
//            System.out.println("Det = 0, no unique solution ........................................................");
        }
        return List.of();
    }

    private static long tokenCount(Solution s) {
        return s.aCount * 3L + s.bCount;
    }

    private static List<ClawMachine> initClawMachines(List<String> lines) {
        List<ClawMachine> machines = new ArrayList<>();
        ClawMachine currentMachine = new ClawMachine();
        for (int i = 0; i < lines.size(); i++) {
            if (i % 4 == 0) {
                Matcher matcher = BUTTON_PATTERN.matcher(lines.get(i));
                matcher.find();
                currentMachine.setXOffsetA(Integer.parseInt(matcher.group(1)));
                currentMachine.setYOffsetA(Integer.parseInt(matcher.group(2)));
            } else if (i % 4 == 1) {
                Matcher matcher = BUTTON_PATTERN.matcher(lines.get(i));
                matcher.find();
                currentMachine.setXOffsetB(Integer.parseInt(matcher.group(1)));
                currentMachine.setYOffsetB(Integer.parseInt(matcher.group(2)));
            } else if (i % 4 == 2) {
                Matcher matcher = PRIZE_PATTERN.matcher(lines.get(i));
                matcher.find();
                currentMachine.setXPrize(10000000000000L + Integer.parseInt(matcher.group(1)));
                currentMachine.setYPrize(10000000000000L + Integer.parseInt(matcher.group(2)));

                machines.add(currentMachine);
                currentMachine = new ClawMachine();
            }
        }
        return machines;
    }

    private record Solution(long aCount, long bCount) {

    }
}
