package com.jdefossez.adventofcode.year2024.days.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day09 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day09.class.getClassLoader().getResource("input/2024/day_09.txt")).toURI());

        List<Integer> data = Arrays.stream(Files.lines(path).toList().getFirst().split(""))
                                   .map(Integer::parseInt)
                                   .toList();

        List<Integer> disk = initDisk(data);

        int i = 0;
        int endIndex = disk.size() - 1;
        Long sum = 0L;
        while (i <= endIndex) {
            int value = disk.get(i);
            while (disk.get(endIndex) < 0) {
                endIndex--;
            }
            value = value == -1 ? disk.get(endIndex--) : value;
            sum += (i * value);
            i++;
        }

        System.out.printf("Result %s", sum);

    }

    private static List<Integer> initDisk(List<Integer> data) {
        List<Integer> disk = new ArrayList<>();
        int fileIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            var value = data.get(i);
            if (i % 2 == 0) {
                // file block
                for (int j = 0; j < value; j++) {
                    disk.add(fileIndex);
                }
                fileIndex++;
            } else {
                // free space block
                for (int j = 0; j < value; j++) {
                    disk.add(-1);
                }
            }
        }

        return disk;
    }

}
