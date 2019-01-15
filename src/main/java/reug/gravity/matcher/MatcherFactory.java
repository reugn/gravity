package reug.gravity.matcher;

@FunctionalInterface
public interface MatcherFactory {
    public Matcher create();
}
