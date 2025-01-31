package com.jdefossez.adventofcode.year2024.days.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day09_2 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day09_2.class.getClassLoader().getResource("input/2024/day_09.txt")).toURI()); // 94524

        List<Integer> disk = Arrays.stream(Files.lines(path).toList().getFirst().split(""))
                                   .map(Integer::parseInt)
                                   .toList();

        InitData data = initData(disk);
        System.out.println(disk.stream().mapToInt(a -> a).sum());

        displayData(data);
        System.out.printf("data : %s\n", data);

        long sum = 0;

        var lastEntry = data.fileBlockSizeById.pollLastEntry();
        while (lastEntry != null) {
            // look for the first large enough free space
            int sizeOfCurrentFile = lastEntry.getValue();
            List<Integer> freeBlockIndexList = data.freeSpaceBlockBySize.get(sizeOfCurrentFile);
            while (sizeOfCurrentFile < 9 && (freeBlockIndexList == null || freeBlockIndexList.isEmpty())) {
                freeBlockIndexList = data.freeSpaceBlockBySize.get(sizeOfCurrentFile++);
            }
            if (freeBlockIndexList != null && !freeBlockIndexList.isEmpty() && freeBlockIndexList.getFirst() < data.fileBlockInitIndexById.get(lastEntry.getKey())) {
                // move the file
                int position = freeBlockIndexList.removeFirst();
                System.out.printf("block %d (pos: %d) will be moved at position %d\n", lastEntry.getKey(), data.fileBlockInitIndexById.get(lastEntry.getKey()), position);
                int endIndexOfMovedFile = position + lastEntry.getValue() - 1;
                sum += lastEntry.getKey() * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (position - 1) * position) / 2);
                System.out.printf("sum of file %d (from %d to %d): %d\n", lastEntry.getKey(), position, endIndexOfMovedFile,
                        lastEntry.getKey() * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (position - 1) * position) / 2));
                int remainingSpaceSize = sizeOfCurrentFile - 1 - lastEntry.getValue();
                if (remainingSpaceSize > 0) {
                    data.freeSpaceBlockBySize.putIfAbsent(remainingSpaceSize, new ArrayList<>());
                    data.freeSpaceBlockBySize.get(remainingSpaceSize).add(position + lastEntry.getValue());
                    data.freeSpaceBlockBySize.get(remainingSpaceSize).sort(Comparator.comparingInt(a -> a));
                }
            } else {
                // do not move the file
                int startPosition = data.fileBlockInitIndexById.get(lastEntry.getKey());
                int endIndexOfMovedFile = startPosition + lastEntry.getValue() - 1;
                sum += lastEntry.getKey() * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (startPosition - 1) * startPosition) / 2);
                System.out.printf("sum of file %d (from %d to %d): %d\n", lastEntry.getKey(), startPosition, endIndexOfMovedFile,
                        lastEntry.getKey() * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (startPosition - 1) * startPosition) / 2));
            }
//            System.out.printf("data : %s\nSum: %d\n\n", data, sum);
//            System.out.println("Sum: " + sum);
            lastEntry = data.fileBlockSizeById.pollLastEntry();
//            break;
        }
        System.out.printf("Result %s", sum);
//         - de 8453634740978
//         + de 6312797359093
    }

    private static InitData initData(List<Integer> data) {
        Map<Integer, List<Integer>> freeSpaceBlockBySize = new HashMap<>();
        TreeMap<Integer, Integer> fileBlockSizeById = new TreeMap<>();
        Map<Integer, Integer> fileBlockInitIndexById = new HashMap<>();

        int diskIndex = 0;
        int fileIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            var value = data.get(i);
            if (i % 2 == 0) {
                // file block
                fileBlockSizeById.put(fileIndex, value);
                fileBlockInitIndexById.put(fileIndex, diskIndex);
                fileIndex++;
            } else {
                // free space block
                if (value != 0) {
                    freeSpaceBlockBySize.putIfAbsent(value, new ArrayList<>());
                    freeSpaceBlockBySize.get(value).add(diskIndex);
                }
            }
            diskIndex += value;
        }
        return new InitData(freeSpaceBlockBySize, fileBlockSizeById, fileBlockInitIndexById);
    }

    private record InitData(Map<Integer, List<Integer>> freeSpaceBlockBySize,
                            TreeMap<Integer, Integer> fileBlockSizeById,
                            Map<Integer, Integer> fileBlockInitIndexById) {

    }

    private static void displayData(InitData data) {
        int filesCount = data.fileBlockSizeById.keySet().size();
        for (int i = 0; i < filesCount; i++) {
//            System.out.println(data.fileBlockSizeById.get(i));
            for (int j = 0; j < data.fileBlockSizeById.get(i); j++) {
                System.out.print(i);
            }
        }
        System.out.println();
    }

}
