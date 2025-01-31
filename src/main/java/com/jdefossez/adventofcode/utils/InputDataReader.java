package com.jdefossez.adventofcode.utils;


import com.jdefossez.adventofcode.common.DataLineMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class InputDataReader<T> {

    public Stream<String> readInputDataFile(String inputDataFileName) throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(InputDataReader.class.getClassLoader().getResource(inputDataFileName)).toURI());
        return Files.lines(path);
    }

    public Stream<T> toObject(String inputDataFileName, DataLineMapper<T> mapper) throws IOException, URISyntaxException {
        return readInputDataFile(inputDataFileName)
                .map(mapper::toObject);
    }
}
