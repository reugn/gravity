package com.github.reugn.gravity.matcher;

@FunctionalInterface
public interface MatcherFactory {
    public Matcher create();
}
