package com.jdefossez.adventofcode.year2024.days.day07;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class Equation {

    private final static Pattern PATTERN = Pattern.compile("^(\\d+):((\\s+\\d+)+)$");

    private Long result;
    private List<Long> operands;

    public static Equation parse(String rawData) {
        Matcher matcher = PATTERN.matcher(rawData);
        matcher.find();

        Long result = Long.parseLong(matcher.group(1));
        List<Long> operands = Arrays.stream(matcher.group(2).trim().split(" ")).map(Long::parseLong).toList();

        return new Equation(result, operands);
    }

    public boolean isSolvable() {
        List<List<Operator>> combinations = Operator.combinations(operands.size() - 1);
        for (List<Operator> operatorList : combinations) {
            if (evaluate(operatorList).equals(result)) {
                return true;
            }
        }
        return false;
    }

    public Long evaluate(List<Operator> operators) {
        Long result = operands.getFirst();
        for (int i = 0; i < operators.size(); i++) {
            result = operators.get(i).function.apply(result, operands.get(i + 1));
        }
        return result;
    }

    public static void main(String[] args) {
        Equation eq = Equation.parse("192: 10 19");
        System.out.printf("Equation : %s\n", eq);

        Operator.combinations(eq.operands.size() - 1)
                .stream()
                .map(eq::evaluate)
                .forEach(System.out::println);
    }

    @Override
    public String toString() {
        return String.format("%d: %s", result, operands);
    }
}
