package com.jdefossez.adventofcode.year2024.days.day08;

public record Coord(int x, int y) {
    public String toString() {
        return String.format("(x=%d, y=%d)", x, y);
    }

    public boolean isValid(int xMax, int yMax) {
        return x >= 0 && x < xMax && y >= 0 && y < yMax;
    }
}
