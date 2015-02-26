package com.github.norbo11.topbuilders.util;

@FunctionalInterface
public interface ModelFinderComparator<T> {
    public boolean compare(T entry, String input);
}
