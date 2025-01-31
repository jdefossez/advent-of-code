package com.jdefossez.adventofcode.year2024.days.day17;

import java.util.Arrays;
import java.util.Optional;

public enum Instruction {
    ADV(0), BXL(1), BST(2), JNZ(3), BXC(4), OUT(5), BDV(6), CDV(7);

    private int opCode;

    Instruction(int opCode) {
        this.opCode = opCode;
    }

    public static Instruction getByOpCode(int opCode) {
        Optional<Instruction> optIns = Arrays.stream(Instruction.values())
                                             .filter(instruction -> instruction.opCode == opCode)
                                             .findFirst();
        return optIns.orElseThrow(() -> new IllegalArgumentException("unknown opCode"));
    }
}
