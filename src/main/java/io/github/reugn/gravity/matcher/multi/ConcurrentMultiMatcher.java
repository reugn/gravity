package io.github.reugn.gravity.matcher.multi;

import com.google.common.base.Preconditions;
import io.github.reugn.gravity.matcher.MatcherFactory;
import io.github.reugn.gravity.model.MatchResult;
import io.github.reugn.gravity.model.Pattern;
import io.github.reugn.gravity.reader.SlidingReader;
import io.github.reugn.gravity.traits.SlidingTargetShift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class ConcurrentMultiMatcher implements CasualMatcher {

    private final MatcherFactory f;
    private final int windowMagnitude;
    private final Executor ex;

    private static final Logger logger = LoggerFactory.getLogger(ConcurrentMultiMatcher.class);

    public ConcurrentMultiMatcher(MatcherFactory f, int windowMagnitude) {
        this.f = f;
        this.windowMagnitude = windowMagnitude;
        this.ex = ForkJoinPool.commonPool();
    }

    public ConcurrentMultiMatcher(MatcherFactory f, int windowMagnitude, Executor ex) {
        this.f = f;
        this.windowMagnitude = windowMagnitude;
        this.ex = ex;
    }

    @Override
    public CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, String target) {
        return CompletableFuture.supplyAsync(() -> invokeAll(patterns, target, SlidingTargetShift.identity).stream()
                .map(r -> r.getPattern().calculateScore(r.getAmount()))
                .reduce(Integer::sum), ex);
    }

    private List<MatchResult> invokeAll(List<Pattern> patterns, String target, SlidingTargetShift s) {
        return patterns.stream().map(p -> toCompletableFuture(p, s.shiftTarget(p, target)))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private CompletableFuture<MatchResult> toCompletableFuture(Pattern p, String target) {
        return CompletableFuture.supplyAsync(() -> new MatchResult(p, f.create().match(p, target)), ex);
    }

    @Override
    public CompletableFuture<Optional<Integer>> match(List<Pattern> patterns, InputStream is) {
        Preconditions.checkArgument(!patterns.isEmpty(), "match patterns can't be empty list");
        return CompletableFuture.supplyAsync(() -> {
                    int w_size = patterns.stream().map(p -> p.getSearch().length()).max(Integer::compareTo).get();
                    List<List<MatchResult>> res = new ArrayList<>();
                    try (SlidingReader reader = new SlidingReader(is, w_size * windowMagnitude)) {
                        Optional<String> window = reader.read();
                        while (window.isPresent()) {
                            res.add(invokeAll(patterns, window.get(), SlidingTargetShift.doShift));
                            window = reader.read();
                        }
                    } catch (IOException e) {
                        logger.error("Exception on read from input stream", e);
                    }
                    return res;
                }, ex
        ).thenApplyAsync(res -> res.stream().flatMap(List::stream)
                .collect(Collectors.groupingBy(MatchResult::getPattern, Collectors.summingInt(MatchResult::getAmount)))
                .entrySet().stream().map(e -> e.getKey().calculateScore(e.getValue())).reduce(Integer::sum), ex);
    }
}
