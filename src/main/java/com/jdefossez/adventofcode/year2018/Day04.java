package com.jdefossez.adventofcode.year2018;

import com.jdefossez.adventofcode.exceptions.InvalidInputDataException;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    public static void main(String[] args) throws IOException {


        List<String> input = Files.readAllLines(Path.of("src/main/resources/input/2018/04.txt"));

        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));

    }

    public static long part1(List<String> input) {

        Map<Integer, Integer> countAsleep = new HashMap<>();
        Map<Integer, int[]> minuteAsleep = new HashMap<>();

        List<Record> records = input.stream().map(Record::parse).sorted(Comparator.comparing(o -> o.date)).toList();

        Integer currentGuardId = 0;
        LocalDateTime lastEventDate = null;

        Pattern patternGuardId = Pattern.compile("Guard #(\\d+) begins shift");
        for (Record r : records) {
            if (r.action.contains("#")) {
                Matcher m = patternGuardId.matcher(r.action);
                m.find();
                currentGuardId = Integer.parseInt(m.group(1));
            } else if (r.action.contains("falls asleep")) {
                lastEventDate = r.date;
            } else if (r.action.contains("wakes up")) {
                if (!countAsleep.containsKey(currentGuardId)) {
                    countAsleep.put(currentGuardId, r.date.getMinute() - lastEventDate.getMinute());
                } else {
                    countAsleep.put(currentGuardId, countAsleep.get(currentGuardId) + (r.date.getMinute() - lastEventDate.getMinute()));
                }

                if (!minuteAsleep.containsKey(currentGuardId)) {
                    minuteAsleep.put(currentGuardId, new int[60]);
                }
                int[] t = minuteAsleep.get(currentGuardId);
                for (int i = lastEventDate.getMinute(); i < r.date.getMinute(); i++) {
                    t[i] = t[i] + 1;
                }
            }
        }

        int idMostAsleep = countAsleep.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).toList().getLast().getKey();
        int indexMostAsleep = 0;
        int maxAlseep = 0;
        int[] t = minuteAsleep.get(idMostAsleep);
        for (int i = 0; i < t.length; i++) {
            if (t[i] > maxAlseep) {
                indexMostAsleep = i;
                maxAlseep = t[i];
            }
        }

        return indexMostAsleep * idMostAsleep;
    }

    public static int part2(List<String> input) {
        Map<Integer, int[]> minuteAsleep = new HashMap<>();

        List<Record> records = input.stream().map(Record::parse).sorted(Comparator.comparing(o -> o.date)).toList();

        Integer currentGuardId = 0;
        LocalDateTime lastEventDate = null;

        Pattern patternGuardId = Pattern.compile("Guard #(\\d+) begins shift");
        for (Record r : records) {
            if (r.action.contains("#")) {
                Matcher m = patternGuardId.matcher(r.action);
                m.find();
                currentGuardId = Integer.parseInt(m.group(1));
            } else if (r.action.contains("falls asleep")) {
                lastEventDate = r.date;
            } else if (r.action.contains("wakes up")) {
                if (!minuteAsleep.containsKey(currentGuardId)) {
                    minuteAsleep.put(currentGuardId, new int[60]);
                }
                int[] t = minuteAsleep.get(currentGuardId);
                for (int i = lastEventDate.getMinute(); i < r.date.getMinute(); i++) {
                    t[i] = t[i] + 1;
                }
            }
        }

        int idMostOften = -1;
        int indexMostAsleep = 0;
        int maxAsleep = 0;

        for (Map.Entry<Integer, int[]> integerEntry : minuteAsleep.entrySet()) {
            int[] t = integerEntry.getValue();
            for (int i = 0; i < t.length; i++) {
                if (t[i] > maxAsleep) {
                    indexMostAsleep = i;
                    maxAsleep = t[i];
                    idMostOften = integerEntry.getKey();
                }
            }
        }

        return idMostOften * indexMostAsleep;
    }

    @AllArgsConstructor
    static class Record {

        static final Pattern pattern = Pattern.compile("^\\[(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+)\\] (.+)$");

        LocalDateTime date;
        String action;

        static Record parse(String input) {
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                return new Record(
                        LocalDateTime.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))),
                        m.group(6));
            }
            throw new InvalidInputDataException(input + " is not a valid Record");
        }

        @Override
        public String toString() {
            return String.format("%s %s", date, action);
        }
    }

}
