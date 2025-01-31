package com.jdefossez.adventofcode.year2015.days;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Wire {

    public Wire(String name, String input) {
        this.name = name;
        this.input = input;
    }

    private String name;

    private boolean resolved;

    private String input;

    private int value;

    public int resolve(Map<String, Wire> states) {

        String[] operation = input.split(" ");

        // Either a wire or a number
        if (operation.length == 1) {
            if (operation[0].matches("\\d+")) {
                // Number
                this.value = resolveNumber(operation[0]);
            } else {
                // Wire
                this.value = resolveWire(states, operation[0]);
            }
        } else if (operation.length == 2) {
            // NOT
            this.value = resolveNot(states, operation[1]);
        } else {
            // AND, OR, LSHIFT, RSHIFT
            this.value = resolveOperation(states, operation);
        }

        this.resolved = true;
        return this.value;
    }

    private Integer resolveOperation(Map<String, Wire> states, String[] operation) {

        int result = 0;
        if (operation[0].matches("\\d+")) {
            // Number
            result = resolveNumber(operation[0]);
        } else {
            // Wire
            result = resolveWire(states, operation[0]);
        }

        switch (operation[1]) {
            case "AND":
                result &= resolveWire(states, operation[2]);
                break;
            case "OR":
                result |= resolveWire(states, operation[2]);
                break;
            case "LSHIFT":
                result <<= resolveNumber(operation[2]);
                break;
            case "RSHIFT":
                result >>= resolveNumber(operation[2]);
                break;
        }

        this.value = result;
        this.resolved = true;
        return this.value;
    }

    private Integer resolveNot(Map<String, Wire> states, String operand) {
        if (operand.matches("\\d+")) {
            // Number
            return ~resolveNumber(operand) & 0xFFFF;
        } else {
            // Wire
            return ~resolveWire(states, operand) & 0xFFFF;
        }
    }

    private Integer resolveWire(Map<String, Wire> states, String wireName) {
        if (states.containsKey(wireName)) {
            if (!states.get(wireName).resolved) {
                this.value = states.get(wireName).resolve(states);
            } else {
                this.value = states.get(wireName).getValue();
            }
            this.resolved = true;
            return this.value;
        }
        throw new InvalidInputException("Wire not found: " + wireName);
    }

    private int resolveNumber(String numberString) {
        this.value = Integer.parseInt(numberString);
        this.resolved = true;
        return this.value;
    }
}
