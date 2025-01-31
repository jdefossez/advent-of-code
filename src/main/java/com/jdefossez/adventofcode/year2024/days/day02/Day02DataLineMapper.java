package com.jdefossez.adventofcode.year2024.days.day02;

import com.jdefossez.adventofcode.common.DataLineMapper;

import java.util.Arrays;

public class Day02DataLineMapper implements DataLineMapper<Day02DataLine> {

    private static final String SEPARATOR = " ";

    @Override
    public Day02DataLine toObject(String data) {
        return new Day02DataLine(Arrays.stream(data.split(SEPARATOR)).map(Integer::parseInt).toList());
    }
}
