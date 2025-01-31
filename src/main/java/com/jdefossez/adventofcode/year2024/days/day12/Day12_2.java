package com.jdefossez.adventofcode.year2024.days.day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Day12_2 {

    private static final int UNVISITED = -1;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day12_2.class.getClassLoader().getResource("input/2024/day_12.txt")).toURI());

        List<String> lines = Files.lines(path).toList();
        int lineCount = lines.size();

        List<List<Plot>> garden = IntStream.range(0, lineCount).mapToObj(row -> {
            String[] types = lines.get(row).split("");
            return IntStream.range(0, types.length).mapToObj(col -> new Plot(UNVISITED, types[col], new ArrayList<>(), row, col)).toList();
        }).toList();

//        List<List<Plot>> garden = Files.lines(path)
//                                       .map(line -> line.split(""))
//                                       .map(array -> Arrays.stream(array).map(s -> new Plot(UNVISITED, s, new ArrayList<>())).collect(Collectors.toList()))
//                                       .toList();

//        garden.forEach(System.out::println);

        List<List<Plot>> regions = new ArrayList<>();
        int currentRegionId = 0;

        for (int row = 0; row < garden.size(); row++) {
            for (int col = 0; col < garden.get(row).size(); col++) {
                Plot plot = garden.get(row).get(col);
                if (plot.getRegionId() == UNVISITED) {
                    regions.add(colonizeRegion(row, col, garden, currentRegionId++));
                }
            }
        }

//        regions.forEach(System.out::println);
//        regions.forEach(Day12_2::countFences);


        regions.stream().map(region -> String.format("Region %d(%s): cost=%d", region.getFirst().getRegionId(), region.getFirst().getType(), getCost(region))).forEach(System.out::println);
        System.out.println("Total cost : " + regions.stream().mapToInt(Day12_2::getCost).sum());
    }

    private static List<Plot> colonizeRegion(int row, int col, List<List<Plot>> garden, int regionId) {
        int gardenWidth = garden.getFirst().size();
        int gardenHeight = garden.size();

        List<Plot> region = new ArrayList<>();

        Plot plot = garden.get(row).get(col);
        plot.setRegionId(regionId);

        String type = plot.getType();

        // is there a fence on East ?
        if (col == gardenWidth - 1 || !garden.get(row).get(col + 1).getType().equals(type)) {
            plot.addFence(Direction.E);
        } else {
            // no fence, so it's the same region
            plot.setEastPlot(garden.get(row).get(col + 1));
            if (garden.get(row).get(col + 1).getRegionId() == UNVISITED) {
                region.addAll(colonizeRegion(row, col + 1, garden, regionId));
            }
        }
        // is there a fence on South ?
        if (row == gardenHeight - 1 || !garden.get(row + 1).get(col).getType().equals(type)) {
            plot.addFence(Direction.S);
        } else {
            plot.setSouthPlot(garden.get(row + 1).get(col));
            if (garden.get(row + 1).get(col).getRegionId() == UNVISITED) {
                region.addAll(colonizeRegion(row + 1, col, garden, regionId));
            }
        }
        // is there a fence on West ?
        if (col == 0 || !garden.get(row).get(col - 1).getType().equals(type)) {
            plot.addFence(Direction.W);
        } else {
            plot.setWestPlot(garden.get(row).get(col - 1));
            if (garden.get(row).get(col - 1).getRegionId() == UNVISITED) {
                region.addAll(colonizeRegion(row, col - 1, garden, regionId));
            }
        }
        // is there a fence on North ?
        if (row == 0 || !garden.get(row - 1).get(col).getType().equals(type)) {
            plot.addFence(Direction.N);
        } else {
            plot.setNorthPlot(garden.get(row - 1).get(col));
            if (garden.get(row - 1).get(col).getRegionId() == UNVISITED) {
                region.addAll(colonizeRegion(row - 1, col, garden, regionId));
            }
        }

        region.add(plot);

        return region;
    }

    private static int countFences(List<Plot> region) {
        // find a starting point with a fence on north
        Map<Direction, Map<Integer, List<Plot>>> plotListsByDirection = new HashMap<>();
        for (Direction dir : Direction.values()) {
            plotListsByDirection.put(dir, new HashMap<>());
        }
        for (Plot plot : region) {
            addPlot(plot, plotListsByDirection);
        }
//        System.out.println(plotListsByDirection);

        plotListsByDirection.forEach(
                (direction, integerListMap) -> integerListMap
                        .forEach((integer, plots) -> {
                            if (direction.equals(Direction.N) || direction.equals(Direction.S)) {
                                sortByCol(plots);
                            } else {
                                sortByRow(plots);
                            }
                        })
        );

        int count = plotListsByDirection.keySet()
                                        .stream()
                                        .mapToInt(direction -> plotListsByDirection.get(direction).values()
                                                                                   .stream()
                                                                                   .mapToInt(list -> countDisjointLists(list, direction))
                                                                                   .sum())
                                        .sum();

//        System.out.println(plotListsByDirection);
//        System.out.println("Resultat: " + count);

        return count;
    }

    private static void addPlot(Plot plot, Map<Direction, Map<Integer, List<Plot>>> plotListsByDirection) {
        plot.getFences().forEach(dir -> {
            if (dir.equals(Direction.N) || dir.equals(Direction.S)) {
                plotListsByDirection.get(dir).putIfAbsent(plot.getRow(), new ArrayList<>());
                plotListsByDirection.get(dir).get(plot.getRow()).add(plot);
            } else {
                plotListsByDirection.get(dir).putIfAbsent(plot.getCol(), new ArrayList<>());
                plotListsByDirection.get(dir).get(plot.getCol()).add(plot);
            }
        });
    }

    private static void sortByCol(List<Plot> plotList) {
        plotList.sort(Comparator.comparingInt(Plot::getCol));
    }

    private static void sortByRow(List<Plot> plotList) {
        plotList.sort(Comparator.comparingInt(Plot::getRow));
    }

    private static int countDisjointLists(List<Plot> plots, Direction dir) {
        int count = 1;
        if (dir.equals(Direction.N) || dir.equals(Direction.S)) {
            int currentCol = plots.getFirst().getCol();
            for (int i = 1; i < plots.size(); i++) {
                if (plots.get(i).getCol() > currentCol + 1) {
                    count++;
                }
                currentCol = plots.get(i).getCol();
            }
        } else {
            int currentRow = plots.getFirst().getRow();
            for (int i = 1; i < plots.size(); i++) {
                if (plots.get(i).getRow() > currentRow + 1) {
                    count++;
                }
                currentRow = plots.get(i).getRow();
            }
        }
        return count;
    }

    private static int getCost(List<Plot> region) {
        return region.size() * countFences(region);
    }

}
