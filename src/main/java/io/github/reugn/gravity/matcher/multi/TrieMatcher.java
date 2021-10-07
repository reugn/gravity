package io.github.reugn.gravity.matcher.multi;

import io.github.reugn.gravity.matcher.multi.trie.Trie;
import io.github.reugn.gravity.model.Pattern;
import io.github.reugn.gravity.reader.TokensReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrieMatcher implements SpecifiedMatcher {

    private final Trie trie;
    private final int tokensPerSlice;
    private final String delimiter;
    private final Executor ex;

    private final Function<String, String> filter;

    private static final Logger logger = LoggerFactory.getLogger(TrieMatcher.class);

    public TrieMatcher(List<Pattern> patterns) {
        this(patterns, 1, " ", ForkJoinPool.commonPool());
    }

    public TrieMatcher(List<Pattern> patterns, int tokensPerSlice) {
        this(patterns, tokensPerSlice, " ", ForkJoinPool.commonPool());
    }

    public TrieMatcher(List<Pattern> patterns, int tokensPerSlice, String delimiter, Executor executor) {
        trie = new Trie(patterns);
        this.tokensPerSlice = tokensPerSlice;
        this.delimiter = delimiter;
        ex = executor;

        List<Function<String, String>> filters = patterns.stream()
                .map(Pattern::getFilter).distinct().collect(Collectors.toList());
        if (filters.size() > 1)
            throw new InvalidParameterException("Mixed filter patterns list not supported.");
        else
            filter = filters.get(0);

    }

    @Override
    public CompletableFuture<Optional<Integer>> match(String target) {
        return CompletableFuture.supplyAsync(() -> trie.match(filter.apply(target)), ex);
    }

    @Override
    public CompletableFuture<Optional<Integer>> match(InputStream is) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Optional<Integer>>> res = new ArrayList<>();
            try (TokensReader reader = new TokensReader(is, tokensPerSlice, delimiter)) {
                Optional<String> sliceOpt = reader.read();
                while (sliceOpt.isPresent()) {
                    res.addAll(matchSublist(sliceOpt.get()));
                    sliceOpt = reader.read();
                }
            } catch (IOException e) {
                logger.error("Exception on read from input stream", e);
            }
            return res.stream().map(CompletableFuture::join)
                    .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                    .reduce(Integer::sum);
        }, ex);
    }

    private List<CompletableFuture<Optional<Integer>>> matchSublist(String slice) {
        return sublistFromSlice(slice).stream().map(this::match).collect(Collectors.toList());
    }

    private List<String> sublistFromSlice(String slice) {
        List<String> res = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        for (String token : slice.split(delimiter)) {
            buf.append(token);
            res.add(buf.toString().trim());
            buf.append(delimiter);
        }
        return res;
    }
}
