package io.github.reugn.gravity.matcher;

import io.github.reugn.gravity.model.Pattern;
import io.github.reugn.gravity.reader.SlidingReader;
import io.github.reugn.gravity.traits.SlidingTargetShift;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseMatcher implements Matcher {

    @Override
    public int match(Pattern pattern, String target) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(target);
        return doMatch(pattern.getSearch(), pattern.transform(target));
    }

    /**
     * The actual match algorithm implementation.
     *
     * @param pattern the pattern to seek.
     * @param target  the transformed target.
     * @return the number of pattern occurrences in the target.
     */
    protected abstract int doMatch(String pattern, String target);

    @Override
    public int match(Pattern pattern, InputStream is, int windowMagnitude) throws IOException {
        int rt = NOT_FOUND;
        try (SlidingReader reader = new SlidingReader(is, pattern.getSearch().length() * windowMagnitude)) {
            Optional<String> window = reader.read();
            while (window.isPresent()) {
                rt += match(pattern, SlidingTargetShift.doShift.shiftTarget(pattern, window.get()));
                window = reader.read();
            }
        }
        return rt;
    }
}
