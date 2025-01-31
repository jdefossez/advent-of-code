package com.jdefossez.adventofcode.year2024.days.day08;

public record Segment(Coord a, Coord b) {

    public int getXLength() {
        return b.x() - a.x();
    }

    public int getYLength() {
        return b.y() - a.y();
    }

    @Override
    public String toString() {
        return "[" + a + ", " + b + "]";
    }
}
