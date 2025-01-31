package com.jdefossez.adventofcode.year2024.days.day14;

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
import java.util.stream.Collectors;

public class Day14 {

    private static final Pattern ROBOT_PATTERN = Pattern.compile("^p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)$");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day14.class.getClassLoader().getResource("input/2024/day_14.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<Robot> robots = initRobots(lines);
//        robots.forEach(System.out::println);

        for (int i = 1; i < 1000000; i++) {
            robots.forEach(robot -> move(robot, 1, 101, 103));
            if (checkContainsLine(robots, 101, 103)) {
                System.out.println("Count : " + i);
                displayRobots(robots, 101, 103);
                break;
            }
        }

//        robots = initRobots(lines);
//
//        robots.forEach(robot -> move(robot, 2830, 101, 103));


//        robots.forEach(System.out::println);

//        System.out.println("Robots count: " + robots.size());


//        System.out.printf("Result : %d", safetyFactor(robots, 101, 103));
    }

    private static int safetyFactor(List<Robot> robots, int width, int height) {
        int xMiddle = width / 2;
        int yMiddle = height / 2;

//        System.out.println("0 -> " + xMiddle + " " + (xMiddle + 1) + " -> " + width);
//        System.out.println("0 -> " + yMiddle + " " + (yMiddle + 1) + " -> " + height);

        int qNW = 0;
        int qNE = 0;
        int qSE = 0;
        int qSW = 0;

        for (Robot robot : robots) {
            if (robot.getX() < xMiddle && robot.getY() < yMiddle) {
                qNW++;
            } else if (robot.getX() < xMiddle && robot.getY() > yMiddle) {
                qSW++;
            } else if (robot.getX() > xMiddle && robot.getY() < yMiddle) {
                qNE++;
            } else if (robot.getX() > xMiddle && robot.getY() > yMiddle) {
                qSE++;
            }
//            System.out.printf("qNE: %d, qSE: %d, qSW: %d, qNW: %d\n", qNE, qSE, qSW, qNW);
        }

        return qSE * qSW * qNE * qNW;
    }

    private static Robot move(Robot robot, int moveCount, int width, int height) {
        int newX = (robot.getX() + robot.getVX() * moveCount) % width;
        int newY = (robot.getY() + robot.getVY() * moveCount) % height;
        robot.setX(newX < 0 ? newX + width : newX);
        robot.setY(newY < 0 ? newY + height : newY);

        return robot;
    }

    private static List<Robot> initRobots(List<String> lines) {

        List<Robot> robots = new ArrayList<>();
        for (String line : lines) {

            Matcher matcher = ROBOT_PATTERN.matcher(line);
            matcher.find();
            Robot currentRobot = new Robot();
            currentRobot.setX(Integer.parseInt(matcher.group(1)));
            currentRobot.setY(Integer.parseInt(matcher.group(2)));
            currentRobot.setVX(Integer.parseInt(matcher.group(3)));
            currentRobot.setVY(Integer.parseInt(matcher.group(4)));

            robots.add(currentRobot);
        }
        return robots;
    }

    private static void displayRobots(List<Robot> robots, int width, int height) {
        List<List<String>> grid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                grid.get(i).add(" ");
            }
        }
        robots.forEach(r -> grid.get(r.getY()).set(r.getX(), "o"));
        for (List<String> s : grid) {
            System.out.println(String.join("", s));
        }
    }

    private static boolean checkContainsLine(List<Robot> robots, int width, int height) {
        List<List<String>> grid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                grid.get(i).add(" ");
            }
        }
        robots.forEach(r -> grid.get(r.getY()).set(r.getX(), "o"));
        for (String s : grid.stream().map(list -> list.stream().collect(Collectors.joining())).toList()) {
            if (s.contains("oooooooo")) {
                return true;
            }
        }
        return false;
    }
}
