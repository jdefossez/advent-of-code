package com.jdefossez.adventofcode.common;

public interface DataLineMapper<T> {

    T toObject(String data);
}
