package com.jdefossez.adventofcode.year2024.days.day01;

import com.jdefossez.adventofcode.common.DataLineMapper;
import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day01DataLineMapper implements DataLineMapper<Day01DataLine> {

    private static final Pattern pattern = Pattern.compile("^(\\d+)\\s+(\\d+)$");

    @Override
    public Day01DataLine toObject(String data) {
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            return new Day01DataLine(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        throw new InvalidInputDataException(String.format("%s is not a valid Day01DAtaLine input data", data));
    }
}
