package com.jdefossez.adventofcode.year2024.days.day17;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Computer {
    private long A;
    private long B;
    private long C;

    private List<Integer> program;

    private int pointer;

    private List<Integer> outputBufferList = new ArrayList<>();
    private String outputBuffer;
    private String programString;

    public String executeProgram() {
        pointer = 0;

        while (pointer < program.size() - 1) {
            Instruction ins = Instruction.getByOpCode(program.get(pointer));
            int operand = program.get(pointer + 1);
            pointer = executeInstruction(ins, operand);
        }

        return outputBuffer;
    }

    private int executeInstruction(Instruction ins, int operand) {
        int newPointer = (int) switch (ins) {
            case ADV -> executeADV(operand);
            case BXL -> executeBXL(operand);
            case BST -> executeBST(operand);
            case JNZ -> executeJNZ(operand);
            case BXC -> executeBXC(operand);
            case OUT -> executeOUT(operand);
            case BDV -> executeBDV(operand);
            case CDV -> executeCDV(operand);
        };
//        printRegisters();
        return newPointer;
    }

    private int executeADV(int operand) {
//        System.out.printf("Execute ADV : A / 2^%s -> A\n", getValueOfComboOperand(operand));
        long denominator = (long) Math.pow(2, getValueOfComboOperand(operand));
        A = A / denominator;
        return pointer + 2;
    }

    private long executeBXL(int operand) {
//        System.out.printf("Execute BXL : B xor %d -> B\n", operand);
        B = B ^ operand;
        return pointer + 2;
    }

    private long executeBST(int operand) {
//        System.out.printf("Execute BST : %s %% 8 -> B\n", getValueOfComboOperand(operand));
        B = getValueOfComboOperand(operand) % 8;
        return pointer + 2;
    }

    private long executeJNZ(int operand) {
        if (A != 0) {
//            System.out.printf("Execute JNZ : jump to %d\n", operand);
            return operand;
        }
        return pointer + 2;
    }

    private long executeBXC(int operand) {
//        System.out.printf("Execute B xor C : %d xor %d -> B\n", B, C);
        B = B ^ C;
        return pointer + 2;
    }

    private long executeOUT(int operand) {
//        System.out.printf("Execute OUT : print %d\n", getValueOfComboOperand(operand) % 8);
        int res = (int) (getValueOfComboOperand(operand) % 8);
        outputBuffer += "," + res;
        outputBufferList.add(res);

//        if (!programString.startsWith(outputBuffer.substring(1))) {
//            return program.size();
//        }
        return pointer + 2;
    }

    private long executeBDV(int operand) {
//        System.out.printf("Execute BDV : A / 2^%s -> B\n", getValueOfComboOperand(operand));
        long denominator = (long) Math.pow(2, getValueOfComboOperand(operand));
        B = A / denominator;
        return pointer + 2;
    }

    private long executeCDV(int operand) {
//        System.out.printf("Execute CDV : A / 2^%s -> C\n", getValueOfComboOperand(operand));
        long denominator = (long) Math.pow(2, getValueOfComboOperand(operand));
        C = A / denominator;
        return pointer + 2;
    }

    private long getValueOfComboOperand(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            default -> throw new IllegalArgumentException(String.format("The value %s should not be present in a valid program", operand));
        };
    }

    public void printRegisters() {
        System.out.printf("Register A:\t%d\nRegister B:\t%d\nRegister C:\t%d\n", A, B, C);
    }
}
