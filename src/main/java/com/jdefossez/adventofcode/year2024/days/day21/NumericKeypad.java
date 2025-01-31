package com.jdefossez.adventofcode.year2024.days.day21;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NumericKeypad {

    private Button ZERO = new Button("0");
    private Button ONE = new Button("1");
    private Button TWO = new Button("2");
    private Button THREE = new Button("3");
    private Button FOUR = new Button("4");
    private Button FIVE = new Button("5");
    private Button SIX = new Button("6");
    private Button SEVEN = new Button("7");
    private Button EIGHT = new Button("8");
    private Button NINE = new Button("9");
    private Button A = new Button("A");

    private Map<Button, List<Button>> graph = new HashMap<>();

    public void initGraph() {
        graph.put(ZERO, List.of(TWO, THREE));
        graph.put(ONE, List.of(TWO, FOUR));
        graph.put(TWO, List.of(ZERO, ONE, THREE, FIVE));
        graph.put(THREE, List.of(A, TWO, SIX));
        graph.put(FOUR, List.of(ONE, FIVE, SEVEN));
        graph.put(FIVE, List.of(TWO, FOUR, SIX, EIGHT));
        graph.put(SIX, List.of(THREE, FIVE, NINE));
        graph.put(SEVEN, List.of(FOUR, EIGHT));
        graph.put(EIGHT, List.of(FIVE, SEVEN, NINE));
        graph.put(NINE, List.of(SIX, EIGHT));
        graph.put(A, List.of(ZERO, THREE));
    }

    public void getShortestPaths(String code) {
        // initial position is A
//        List<Button> path =
    }
}
