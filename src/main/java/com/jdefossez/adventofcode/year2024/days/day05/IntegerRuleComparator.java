package com.jdefossez.adventofcode.year2024.days.day05;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class IntegerRuleComparator implements Comparator<Integer> {

    List<Rule> rules;

    public IntegerRuleComparator(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        Optional<Rule> rulePos = rules.stream().filter(r -> r.a() == o1 && r.b() == o2).findFirst();
        if (rulePos.isPresent()) return -1;

        Optional<Rule> ruleNeg = rules.stream().filter(r -> r.a() == o2 && r.b() == o1).findFirst();
        if (ruleNeg.isPresent()) return 1;

        return 0;
    }
}