package com.jdefossez.adventofcode.year2016.days;

import com.jdefossez.adventofcode.utils.MD5;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05 {

    public static void main(String[] args) throws IOException {

        String doorId = "cxdnnyjw";

        System.out.println("Part 1: " + part1(doorId));
        System.out.println("Part 2: " + part2(doorId));

    }

    public static String part1(String doorId) {
        return Stream.iterate(0, i -> i + 1)
                     .map(i -> MD5.getMD5(doorId + i))
                     .filter(s -> s.startsWith("00000"))
                     .map(s -> s.substring(5, 6))
                     .limit(8)
                     .collect(Collectors.joining());
    }

    public static String part2(String doorId) {
        int[] visited = new int[8];
        return //Arrays.stream(
                Stream.iterate(0, i -> i + 1)
                      .map(i -> MD5.getMD5(doorId + i))
                      .filter(s -> s.startsWith("00000"))
                      .filter(s -> s.substring(5, 6).matches("^[0-7]$"))
                      .filter(s -> visited[Integer.parseInt(s.substring(5, 6))] == 0)
                      .peek(s -> visited[Integer.parseInt(s.substring(5, 6))] = 1)
                      .map(s -> s.substring(5, 7))
                      .limit(8)
                      .collect(() -> new StringBuilder("        "),
                              (sb, s) -> {
                                  int position = Integer.parseInt(s.substring(0, 1));
                                  char c = s.charAt(1);
                                  sb.setCharAt(position, c);
                              },
                              StringBuilder::append)
                      .toString()

//                                   .collect(() -> new Character[8], (strings, s) -> {
//                                       int position = Integer.parseInt(s.substring(0, 1));
//                                       char c = s.charAt(1);
//                                       strings[position] = c;
//                                   }, (strings1, strings2) -> {
//                                       for (int i = 0; i < strings2.length; i++) {
//                                           if (strings2[i] != null) {
//                                               strings1[i] = strings2[i];
//                                           }
//                                       }
//                                   })

//                     )
//                     .map(Object::toString)
//                     .collect(Collectors.joining());
                ;
    }
}
