package com.jdefossez.adventofcode.year2024.days.day06;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {

    private Coord coord;
    private Dir dir;

    public State(Coord coord, Dir dir) {
        this.coord = coord;
        this.dir = dir;
    }

    @Override
    public String toString() {
        return String.format("%s %s", coord, dir);
    }
}
