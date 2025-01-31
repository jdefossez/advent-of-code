package com.jdefossez.adventofcode.year2024.days.day15;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coord {
    private int x;
    private int y;

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public void moveToNorth() {
        y--;
    }

    public void moveToEast() {
        x++;
    }

    public void moveToSouth() {
        y++;
    }

    public void moveToWest() {
        x--;
    }
}