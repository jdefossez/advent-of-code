package com.jdefossez.adventofcode.year2024.days.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day09_3 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day09_3.class.getClassLoader().getResource("input/2024/day_09.txt")).toURI()); // 94524

        List<Integer> disk = Arrays.stream(Files.lines(path).toList().getFirst().split(""))
                                   .map(Integer::parseInt)
                                   .toList();

        InitData data = initData(disk);
        System.out.println(disk.stream().mapToInt(a -> a).sum());

//        displayData(data);
//        System.out.printf("data : %s\n", data);

        long sum = 0;

        var lastEntry = data.fileBlockSizeById.pollLastEntry();
        while (lastEntry != null) {
            // look for the first large enough free space
            int fileId = lastEntry.getKey();
            int sizeOfCurrentFile = lastEntry.getValue();
            BlockInfo firstFreeSpaceBlockInfo = getFirstFreeSpaceBlockIndex(sizeOfCurrentFile, data, fileId);

            if (firstFreeSpaceBlockInfo != null) {
                // move the file
//                System.out.printf("block %d (pos: %d) will be moved at firstFreeSpaceBlockInfo.pos %d\n", fileId, data.fileBlockInitIndexById.get(fileId), firstFreeSpaceBlockInfo.pos);
                int fileAfterMovedEndIndex = firstFreeSpaceBlockInfo.pos + lastEntry.getValue() - 1;
                sum += fileId * (((long) fileAfterMovedEndIndex * (fileAfterMovedEndIndex + 1)) / 2 - ((long) (firstFreeSpaceBlockInfo.pos - 1) * firstFreeSpaceBlockInfo.pos) / 2);
//                System.out.printf("sum of file %d (from %d to %d): %d\n", fileId, firstFreeSpaceBlockInfo.pos, endIndexOfMovedFile,
//                        fileId * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (firstFreeSpaceBlockInfo.pos - 1) * firstFreeSpaceBlockInfo.pos) / 2));
                int remainingSpaceSize = firstFreeSpaceBlockInfo.size - lastEntry.getValue();
                if (remainingSpaceSize > 0) {
                    data.freeSpaceBlockBySize.putIfAbsent(remainingSpaceSize, new ArrayList<>());
                    data.freeSpaceBlockBySize.get(remainingSpaceSize).add(firstFreeSpaceBlockInfo.pos + lastEntry.getValue());
                    data.freeSpaceBlockBySize.get(remainingSpaceSize).sort(Comparator.comparingInt(a -> a));
                }
                for (int i = 0; i < lastEntry.getValue(); i++) {
                    data.disk.set(i + firstFreeSpaceBlockInfo.pos, fileId);
                    data.disk.set(i + data.fileBlockInitIndexById.get(fileId), -1);
                }
            } else {
                // do not move the file
                int startPosition = data.fileBlockInitIndexById.get(fileId);
                int endIndexOfMovedFile = startPosition + lastEntry.getValue() - 1;
                sum += fileId * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (startPosition - 1) * startPosition) / 2);
//                System.out.printf("sum of file %d (from %d to %d): %d\n", fileId, startPosition, endIndexOfMovedFile,
//                        fileId * (((long) endIndexOfMovedFile * (endIndexOfMovedFile + 1)) / 2 - ((long) (startPosition - 1) * startPosition) / 2));
            }
//            displayData(data);
//            System.out.printf("data : %s\nSum: %d\n\n", data, sum);
//            System.out.println("Sum: " + sum);
            lastEntry = data.fileBlockSizeById.pollLastEntry();
        }
        System.out.printf("Result %s", sum);
    }

    private static BlockInfo getFirstFreeSpaceBlockIndex(int blockSize, InitData data, int fileId) {
        int firstFreeSpaceBlockIndex = Integer.MAX_VALUE;
        int firstFreeSpaceBlockSize = 0;
        for (int size = blockSize; size <= 9; size++) {
            if (data.freeSpaceBlockBySize.containsKey(size)) {
                int firstIndex = data.freeSpaceBlockBySize.get(size).getFirst();
                if (firstIndex < firstFreeSpaceBlockIndex) {
                    firstFreeSpaceBlockIndex = firstIndex;
                    firstFreeSpaceBlockSize = size;
                }
            }
        }
        if (firstFreeSpaceBlockIndex == Integer.MAX_VALUE) {
            return null;
        }
        if (firstFreeSpaceBlockIndex < data.fileBlockInitIndexById.get(fileId)) {
            data.freeSpaceBlockBySize.get(firstFreeSpaceBlockSize).removeFirst();
            if (data.freeSpaceBlockBySize.get(firstFreeSpaceBlockSize).isEmpty()) {
                data.freeSpaceBlockBySize.remove(firstFreeSpaceBlockSize);
            }
            return new BlockInfo(firstFreeSpaceBlockIndex, firstFreeSpaceBlockSize);
        }
        return null;
    }

    private record BlockInfo(int pos, int size) {

    }

    private static InitData initData(List<Integer> data) {
        Map<Integer, List<Integer>> freeSpaceBlockBySize = new HashMap<>();
        TreeMap<Integer, Integer> fileBlockSizeById = new TreeMap<>();
        Map<Integer, Integer> fileBlockInitIndexById = new HashMap<>();

        List<Integer> disk = new ArrayList<>();
        int diskIndex = 0;
        int fileIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            var value = data.get(i);
            if (i % 2 == 0) {
                // file block
                fileBlockSizeById.put(fileIndex, value);
                fileBlockInitIndexById.put(fileIndex, diskIndex);
                for (int j = diskIndex; j < diskIndex + value; j++) {
                    disk.add(fileIndex);
                }
                fileIndex++;
            } else {
                // free space block
                if (value != 0) {
                    freeSpaceBlockBySize.putIfAbsent(value, new ArrayList<>());
                    freeSpaceBlockBySize.get(value).add(diskIndex);
                }
                for (int j = diskIndex; j < diskIndex + value; j++) {
                    disk.add(-1);
                }
            }
            diskIndex += value;
        }
        return new InitData(freeSpaceBlockBySize, fileBlockSizeById, fileBlockInitIndexById, disk);
    }

    private record InitData(Map<Integer, List<Integer>> freeSpaceBlockBySize,
                            TreeMap<Integer, Integer> fileBlockSizeById,
                            Map<Integer, Integer> fileBlockInitIndexById,
                            List<Integer> disk) {

    }

    private static void displayData(InitData data) {
        System.out.println(data.disk.stream()
                                    .map(a -> a >= 0 ? a.toString() : ".")
                                    .collect(Collectors.joining()));
    }

}
