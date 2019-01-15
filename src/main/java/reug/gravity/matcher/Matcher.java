package reug.gravity.matcher;

import reug.gravity.model.Pattern;

import java.io.IOException;
import java.io.InputStream;

public interface Matcher {

    int NOT_FOUND = 0;

    /**
     * String target data matching
     *
     * @param pattern pattern to match
     * @param target  target input
     * @return amount of matches
     */
    int match(Pattern pattern, String target);

    /**
     * InputStream target data matching
     *
     * @param pattern         pattern to match
     * @param is              InputStream as a target
     * @param windowMagnitude sliding window size multiplier
     * @return amount of matches
     * @throws IOException on input stream read
     */
    int match(Pattern pattern, InputStream is, int windowMagnitude) throws IOException;
}
