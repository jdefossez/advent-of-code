package com.jdefossez.adventofcode.year2024.days.day14;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Robot {

    private int x;
    private int y;
    private int vX;
    private int vY;

    @Override
    public String toString() {
        return String.format("p=%d,%d v=%d,%d", x, y, vX, vY);
    }
}
