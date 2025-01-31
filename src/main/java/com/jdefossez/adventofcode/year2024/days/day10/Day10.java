package com.jdefossez.adventofcode.year2024.days.day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day10 {

    static int pathCount = 0;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day10.class.getClassLoader().getResource("input/2024/day_10.txt")).toURI());

        List<List<Integer>> data = Files.lines(path)
                                        .map(line -> line.split(""))
                                        .map(array -> Arrays.stream(array).map(Integer::parseInt).toList())
                                        .toList();


        TopographicMap map = new TopographicMap(data.getFirst().size() - 1, data.size() - 1, data);

        List<Node> trailheadList = new ArrayList<>();
        for (int row = 0; row <= map.yMax; row++) {
            for (int col = 0; col <= map.xMax; col++) {
                if (map.data.get(row).get(col) == 0) {
                    trailheadList.add(buildNode(col, row, map, null));
                }
            }
        }

//        System.out.println(trailheadList);
        int sum = trailheadList.stream().map(Node::summitList).mapToInt(Set::size).sum();
        System.out.printf("Result %s, pathCount: %d", sum, pathCount);

    }

    private static Node buildNode(int x, int y, TopographicMap map, Node root) {
//        System.out.printf("build node (%d, %d)\n", x, y);
        int height = map.data.get(y).get(x);
        Node newNode = new Node(height, x, y, new ArrayList<>(), new HashSet<>());

        if (height == 9) {
            root.summitList.add(newNode);
            pathCount++;
        }

        // East
        if (x < map.xMax && map.data.get(y).get(x + 1) == height + 1) {
            newNode.children.add(buildNode(x + 1, y, map, root != null ? root : newNode));
        }
        // South
        if (y < map.yMax && map.data.get(y + 1).get(x) == height + 1) {
            newNode.children.add(buildNode(x, y + 1, map, root != null ? root : newNode));
        }
        // West
        if (x > 0 && map.data.get(y).get(x - 1) == height + 1) {
            newNode.children.add(buildNode(x - 1, y, map, root != null ? root : newNode));
        }
        // North
        if (y > 0 && map.data.get(y - 1).get(x) == height + 1) {
            newNode.children.add(buildNode(x, y - 1, map, root != null ? root : newNode));
        }
        return newNode;
    }

    private record TopographicMap(int xMax, int yMax, List<List<Integer>> data) {

    }

    private record Node(int height, int x, int y, List<Node> children, Set<Node> summitList) {
//        public String toString() {
//            return String.format("(x=%d, y=%d)", x, y);
//        }

        public boolean isValid(int xMax, int yMax) {
            return x >= 0 && x < xMax && y >= 0 && y < yMax;
        }
    }

}
