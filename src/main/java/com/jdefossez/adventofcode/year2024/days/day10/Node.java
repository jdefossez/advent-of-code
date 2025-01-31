package com.jdefossez.adventofcode.year2024.days.day10;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class Node {
    int height;
    int x;
    int y;
    List<Node> children;
    Set<Node> summitList;
    int rating;

//        public String toString() {
//            return String.format("(x=%d, y=%d)", x, y);
//        }


}