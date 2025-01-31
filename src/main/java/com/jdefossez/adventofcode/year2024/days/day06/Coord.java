package com.jdefossez.adventofcode.year2024.days.day06;

public record Coord(int x, int y) {
    public String toString() {
        return String.format("(x=%d, y=%d)", x, y);
    }
}
