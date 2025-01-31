package com.jdefossez.adventofcode.year2024.days.day12;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Plot {
    private int row;
    private int col;
    private int regionId;
    private String type;
    //private int fenceCount;
    private List<Direction> fences;
    private Plot eastPlot;
    private Plot southPlot;
    private Plot westPlot;
    private Plot northPlot;

    public Plot(int regionId, String type, List<Direction> fences, int row, int col) {
        this.regionId = regionId;
        this.type = type;
        this.fences = fences;
        this.row = row;
        this.col = col;
    }

    public void addFence(Direction fence) {
        this.fences.add(fence);
    }

    public int getFenceCount() {
        return this.fences.size();
    }

    @Override
    public String toString() {
        return String.format("(%d %d)", row, col);
    }
}