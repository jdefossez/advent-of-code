package com.jdefossez.adventofcode.year2024.days.day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day12 {

    private static final int UNVISITED = -1;

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Day12.class.getClassLoader().getResource("input/2024/day_12.txt")).toURI());

//        List<List<Plot>> garden = Files.lines(path)
//                                       .map(line -> line.split(""))
//                                       .map(array -> Arrays.stream(array).map(s -> new Plot(UNVISITED, s, new ArrayList<>())).collect(Collectors.toList()))
//                                       .toList();

//        garden.forEach(System.out::println);

        List<List<Plot>> regions = new ArrayList<>();
        int currentRegionId = 0;

//        for (int row = 0; row < garden.size(); row++) {
//            for (int col = 0; col < garden.get(row).size(); col++) {
//                Plot plot = garden.get(row).get(col);
//                if (plot.getRegionId() == UNVISITED) {
//                    regions.add(colonizeRegion(row, col, garden, currentRegionId++));
//                }
//            }
//        }

        regions.forEach(System.out::println);

        regions.stream().map(region -> String.format("Region %s: cost=%d", region.getFirst().getRegionId(), getCost(region))).forEach(System.out::println);
        System.out.println("Total cost : " + regions.stream().mapToInt(Day12::getCost).sum());
    }

    private static List<Plot> colonizeRegion(int row, int col, List<List<Plot>> garden, int regionId) {
        int gardenWidth = garden.getFirst().size();
        int gardenHeight = garden.size();

        List<Plot> region = new ArrayList<>();

        Plot plot = garden.get(row).get(col);
        plot.setRegionId(regionId);

        String type = plot.getType();

//        // is there a fence on East ?
//        if (col == gardenWidth - 1 || !garden.get(row).get(col + 1).getType().equals(type)) {
//            plot.setFenceCount(plot.getFenceCount() + 1);
//        } else {
//            // no fence, so it's the same region
//            if (garden.get(row).get(col + 1).getRegionId() == UNVISITED) {
//                region.addAll(colonizeRegion(row, col + 1, garden, regionId));
//            }
//        }
//        // is there a fence on South ?
//        if (row == gardenHeight - 1 || !garden.get(row + 1).get(col).getType().equals(type)) {
//            plot.setFenceCount(plot.getFenceCount() + 1);
//        } else {
//            if (garden.get(row + 1).get(col).getRegionId() == UNVISITED) {
//                region.addAll(colonizeRegion(row + 1, col, garden, regionId));
//            }
//        }
//        // is there a fence on West ?
//        if (col == 0 || !garden.get(row).get(col - 1).getType().equals(type)) {
//            plot.setFenceCount(plot.getFenceCount() + 1);
//        } else {
//            if (garden.get(row).get(col - 1).getRegionId() == UNVISITED) {
//                region.addAll(colonizeRegion(row, col - 1, garden, regionId));
//            }
//        }
//        // is there a fence on North ?
//        if (row == 0 || !garden.get(row - 1).get(col).getType().equals(type)) {
//            plot.setFenceCount(plot.getFenceCount() + 1);
//        } else {
//            if (garden.get(row - 1).get(col).getRegionId() == UNVISITED) {
//                region.addAll(colonizeRegion(row - 1, col, garden, regionId));
//            }
//        }

        region.add(plot);

        return region;
    }

    private static int getCost(List<Plot> region) {
        return region.size() * (region.stream().mapToInt(Plot::getFenceCount).sum());
    }

}
