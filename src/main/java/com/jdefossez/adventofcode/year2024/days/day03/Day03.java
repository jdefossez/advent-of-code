package com.jdefossez.adventofcode.year2024.days.day03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 {

    private static final Pattern patternMul = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private static final Pattern patternDo = Pattern.compile("do\\(\\)");
    private static final Pattern patternDont = Pattern.compile("don't\\(\\)");
    private static final Pattern patternDoOrNot = Pattern.compile("do\\(\\)|don't\\(\\)");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day03.class.getClassLoader().getResource("input/2024/day_03.txt")).toURI());
        var result1 = Files.lines(path)
                           .flatMapToInt(line -> {
                               return patternMul.matcher(line).results()
                                                .map(r -> line.substring(r.start(), r.end()))
                                                .mapToInt(s -> {
                                                    var m = patternMul.matcher(s);
                                                    m.find();
                                                    int a = Integer.parseInt(m.group(1));
                                                    int b = Integer.parseInt(m.group(2));
                                                    return a * b;
                                                });
                           })
                           .sum();

        System.out.printf("Result 1: %s\n", result1);


        // Part 2
        AtomicBoolean enabled = new AtomicBoolean(true);

        String oneLine = Files.lines(path).collect(Collectors.joining());

        var result2 = patternMul.matcher(oneLine).results()
                                .mapToInt(result -> {
                                    var mulString = oneLine.substring(result.start(), result.end());

                                    var listMatchResults = patternDoOrNot.matcher(oneLine.substring(0, result.start())).results().toList();
                                    var matchResultDoOrNot = !listMatchResults.isEmpty() ? listMatchResults.get(listMatchResults.size() - 1) : null;

                                    enabled.set(listMatchResults.isEmpty()
                                            || oneLine.substring(matchResultDoOrNot.start(), matchResultDoOrNot.end()).matches(patternDo.pattern()));

                                    if (enabled.get()) {
                                        var m = patternMul.matcher(mulString);
                                        m.find();
                                        int a = Integer.parseInt(m.group(1));
                                        int b = Integer.parseInt(m.group(2));
                                        return a * b;
                                    }
                                    return 0;
                                })
                                .sum();
        System.out.printf("Result 2: %s", result2);
    }
}
