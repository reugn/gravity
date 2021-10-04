package io.github.reugn.gravity.matcher;

import java.util.Arrays;

public class LevenshteinMatcher extends BaseMatcher {

    private final int graceDistance;
    private final String delimiter;

    public LevenshteinMatcher(int graceDistance) {
        this(graceDistance, " ");
    }

    public LevenshteinMatcher(int graceDistance, String delimiter) {
        this.graceDistance = graceDistance;
        this.delimiter = delimiter;
    }

    @Override
    protected int doMatch(String pattern, String target) {
        int rt = 0;
        String[] tokens = target.split(delimiter);
        for (String t : tokens) {
            if (hasMatch(pattern, t))
                rt++;
        }
        return rt;
    }

    private boolean hasMatch(String pattern, String token) {
        if (Math.abs(pattern.length() - token.length()) > graceDistance) {
            return false;
        } else {
            return distance(pattern, token) <= graceDistance;
        }
    }

    private int distance(String pattern, String token) {
        if (pattern.equals(token))
            return 0;
        if (pattern.length() == 0)
            return token.length();
        if (token.length() == 0)
            return pattern.length();

        int[] pcp = pattern.codePoints().toArray();
        int[] tcp = token.codePoints().toArray();

        int[] v0 = new int[tcp.length + 1];
        int[] v1 = new int[tcp.length + 1];
        Arrays.setAll(v0, i -> i);

        for (int i = 0; i < pcp.length; i++) {
            v1[0] = i + 1;
            for (int j = 0; j < tcp.length; j++) {
                int cost = (pcp[i] == tcp[j]) ? 0 : 1;
                v1[j + 1] = min(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }
            Arrays.setAll(v0, j -> v1[j]);
        }
        return v1[tcp.length];
    }

    private int min(int... ints) {
        return Arrays.stream(ints).min().getAsInt();
    }
}
