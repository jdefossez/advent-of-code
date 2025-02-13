package com.jdefossez.adventofcode.year2017;

import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2017/08.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static String part1(List<String> input) {
        Map<String, Integer> allRegisters = new HashMap<>();

        input.stream().map(Instruction::parse).forEach(i -> apply(i, allRegisters));

        return String.valueOf(allRegisters.values().stream().mapToInt(Integer::intValue).max().getAsInt());
    }


    public static String part2(List<String> input) {

        Map<String, Integer> allRegisters = new HashMap<>();

        return String.valueOf(input.stream().map(Instruction::parse).mapToInt(i -> apply(i, allRegisters)).summaryStatistics().getMax());
    }

    private static int apply(Instruction instruction, Map<String, Integer> allRegisters) {
        if (evaluate(instruction.condition, allRegisters)) {
            int registerValue = allRegisters.getOrDefault(instruction.register, 0);
            int newValue = switch (instruction.operation) {
                case "inc" -> registerValue + instruction.amount;
                case "dec" -> registerValue - instruction.amount;
                default -> throw new InvalidInputDataException(instruction.operation + " is not a valid operation");
            };
            allRegisters.put(instruction.register, newValue);
            return newValue;
        }
        return 0;
    }

    private static boolean evaluate(Condition condition, Map<String, Integer> allRegisters) {
        int registerValue = allRegisters.getOrDefault(condition.register, 0);

        return switch (condition.operator) {
            case "<" -> registerValue < condition.value;
            case ">" -> registerValue > condition.value;
            case "==" -> registerValue == condition.value;
            case "<=" -> registerValue <= condition.value;
            case ">=" -> registerValue >= condition.value;
            case "!=" -> registerValue != condition.value;
            default -> throw new InvalidInputDataException(condition.operator + " is not a valid operator");
        };
    }

    @AllArgsConstructor
    static class Instruction {

        static final Pattern pattern = Pattern.compile("^(\\w+) (inc|dec) (-?\\d+) if (.*)$");

        String register;
        String operation;
        Integer amount;
        Condition condition;

        static Instruction parse(String input) {
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                String register = m.group(1);
                String operation = m.group(2);
                Integer amount = Integer.parseInt(m.group(3));

                Condition condition = Condition.parse(m.group(4).trim());

                return new Instruction(register, operation, amount, condition);
            }
            throw new InvalidInputDataException(input + " is not a valid instruction");
        }
    }

    @AllArgsConstructor
    static class Condition {

        static final Pattern pattern = Pattern.compile("^(\\w+) ([!<>=]+) (-?\\d+)$");

        String register;
        String operator;
        Integer value;

        static Condition parse(String input) {
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                String register = m.group(1);
                String operation = m.group(2);
                Integer amount = Integer.parseInt(m.group(3));

                return new Condition(register, operation, amount);
            }
            throw new InvalidInputDataException(input + " is not a valid condition");
        }
    }
}
