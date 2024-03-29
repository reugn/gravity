package io.github.reugn.gravity.model;

import java.util.function.Function;

public class Pattern {

    private final String search;
    private final int score;
    private final int max_spot;

    private final Function<String, String> filter;

    public Pattern(String search) {
        this(search, 1);
    }

    public Pattern(String search, int score) {
        this(search, score, 1);
    }

    public Pattern(String search, int score, int max_spot) {
        this(search, score, max_spot, Function.identity());
    }

    public Pattern(String search, int score, int max_spot, Function<String, String> filter) {
        this.search = filter.apply(search);
        this.score = score;
        this.max_spot = max_spot;
        this.filter = filter;
    }

    public String transform(String search) {
        return filter.apply(search);
    }

    public String getSearch() {
        return search;
    }

    public int getScore() {
        return score;
    }

    public int getMax() {
        return max_spot;
    }

    public Function<String, String> getFilter() {
        return filter;
    }

    public int calculateScore(int matches) {
        return matches > max_spot ? max_spot * score : matches * score;
    }
}
