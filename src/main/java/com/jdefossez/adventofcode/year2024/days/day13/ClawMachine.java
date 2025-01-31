package com.jdefossez.adventofcode.year2024.days.day13;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClawMachine {
    long xOffsetA, yOffsetA, xOffsetB, yOffsetB, xPrize, yPrize;

    @Override
    public String toString() {
        return String.format("""
                Button A: X+%d, Y+%d
                Button B: X+%d, Y+%d
                Prize: X=%d, Y=%d""", xOffsetA, yOffsetA, xOffsetB, yOffsetB, xPrize, yPrize);
    }
}
