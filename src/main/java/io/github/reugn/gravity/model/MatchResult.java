package io.github.reugn.gravity.model;

public class MatchResult {

    private final Pattern pattern;
    private final int amount;

    public MatchResult(Pattern pattern, int amount) {
        this.pattern = pattern;
        this.amount = amount;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getAmount() {
        return amount;
    }
}
