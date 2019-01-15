package reug.gravity.matcher;

import reug.gravity.model.Pattern;
import reug.gravity.traits.SlidingTargetShift;
import reug.gravity.util.SlidingReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public abstract class BaseMatcher implements Matcher {

    @Override
    public int match(Pattern pattern, String target) {
        return doMatch(pattern.getSearch(), pattern.transform(target));
    }

    /**
     * Actual match algorithm implementation
     *
     * @param pattern to seek
     * @param target  transformed target
     * @return number of pattern occurrences in target
     */
    protected abstract int doMatch(String pattern, String target);

    @Override
    public int match(Pattern pattern, InputStream is, int windowMagnitude) throws IOException {
        int rt = NOT_FOUND;
        try (SlidingReader reader = new SlidingReader(is, pattern.getSearch().length() * windowMagnitude)) {
            Optional<String> window = reader.read();
            while (window != Optional.<String>empty()) {
                rt += match(pattern, SlidingTargetShift.doShift.shiftTarget(pattern, window.get()));
                window = reader.read();
            }
        }
        return rt;
    }
}
