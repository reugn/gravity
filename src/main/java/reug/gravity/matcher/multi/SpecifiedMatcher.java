package reug.gravity.matcher.multi;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SpecifiedMatcher {

    /**
     * Search for patterns in String target
     *
     * @param target target input
     * @return CompletableFuture of score
     */
    CompletableFuture<Optional<Integer>> match(String target);

    /**
     * Stream based pattern matching
     *
     * @param is InputStream as a target
     * @return CompletableFuture of score
     */
    CompletableFuture<Optional<Integer>> match(InputStream is);
}
