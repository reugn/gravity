package reug.gravity.model;

public class MatchResult {

    private Pattern pattern;
    private int amount;

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
