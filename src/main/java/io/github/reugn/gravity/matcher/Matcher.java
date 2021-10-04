package io.github.reugn.gravity.matcher;

import io.github.reugn.gravity.model.Pattern;

import java.io.IOException;
import java.io.InputStream;

public interface Matcher {

    int NOT_FOUND = 0;

    /**
     * Looks for the <code>pattern</code> in the <code>target</code> string.
     *
     * <p> Calculates and returns the number of the pattern occurrences.
     *
     * @param pattern the pattern to look for.
     * @param target  the target string input.
     * @return the number of occurrences of the given pattern in the target string.
     */
    int match(Pattern pattern, String target);

    /**
     * Looks for the <code>pattern</code> in the <code>is</code> InputStream.
     *
     * <p> Calculates and returns the number of the pattern occurrences.
     *
     * @param pattern         the pattern to look for.
     * @param is              the InputStream as a target.
     * @param windowMagnitude the sliding window size multiplier.
     * @return the number of occurrences of the given pattern in the target InputStream.
     * @throws IOException if an InputStream I/O error occurs.
     */
    int match(Pattern pattern, InputStream is, int windowMagnitude) throws IOException;
}
