package com.jdefossez.adventofcode.year2024.days.day06;

public enum Dir {
    N, E, S, W;

    public Dir turnRight() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }
}
