package io.github.reugn.gravity.matcher.multi;

import io.github.reugn.gravity.model.Pattern;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CasualMatcher {

    /**
     * Looks for the <code>patterns</code> in the String <code>target</code>.
     *
     * @param patterns the list of the pattern objects to look for.
     * @param target   the String target source.
     * @return the <code>CompletableFuture</code> of the Optional result score.
     */
    CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, String target);

    /**
     * Looks for the <code>patterns</code> in the InputStream <code>is</code>.
     *
     * @param patterns the list of the pattern objects to look for.
     * @param is       the InputStream target source.
     * @return the <code>CompletableFuture</code> of the Optional result score.
     */
    CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, InputStream is);
}
