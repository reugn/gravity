package reug.gravity.matcher.multi;

import reug.gravity.model.Pattern;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface MultiMatcher {

    /**
     * Search for patterns in String target
     *
     * @param patterns list of pattern objects to match
     * @param target   target input
     * @return CompletableFuture of score
     */
    CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, String target);

    /**
     * Stream based pattern matching
     *
     * @param patterns list of pattern objects to match
     * @param is       InputStream as a target
     * @return CompletableFuture of score
     */
    CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, InputStream is);
}
