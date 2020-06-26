package com.github.reugn.gravity.matcher.multi;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SpecifiedMatcher {

    /**
     * Scans the String <code>target</code> to calculate and return the match result score.
     *
     * @param target the String target source.
     * @return the <code>CompletableFuture</code> of the Optional result score.
     */
    CompletableFuture<Optional<Integer>> match(String target);

    /**
     * Scans the InputStream <code>is</code> to calculate and return the match result score.
     *
     * @param is the InputStream target source.
     * @return the <code>CompletableFuture</code> of the Optional result score.
     */
    CompletableFuture<Optional<Integer>> match(InputStream is);
}
