package com.jdefossez.adventofcode.year2024.days.day16;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        if (x == 13 && y == 1) {
            System.out.println("Hash = " + Objects.hash(x, y));
        }
        return Objects.hash(x, y);
    }
}