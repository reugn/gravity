package io.github.reugn.gravity.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

public class SlidingReader implements AutoCloseable {

    /**
     * Underlying Input stream.
     */
    private final InputStream _is;

    /**
     * Sliding interval size.
     */
    private final int slidingInterval;

    /**
     * Stream reading buffer.
     */
    private final byte[] cbuf;

    /**
     * The actual sliding window object.
     * Has <code>slidingInterval * 2<code/> length and slides by <code>slidingInterval<code/>.
     */
    private String window;

    public SlidingReader(InputStream is, int sliding_interval) {
        _is = new BufferedInputStream(is);
        slidingInterval = sliding_interval;
        cbuf = new byte[sliding_interval];
        window = String.join("", Collections.nCopies(sliding_interval, " "));
    }

    /**
     * Reads from the underlying InputStream and returns a sliding window.
     * <p>
     * (previous) aaaaabbbbb
     * <p>
     * (read) ccccc
     * <p>
     * (return) bbbbbccccc
     *
     * @return Optional of a sliding window or <code>Optional.empty()<code/> on the end of stream.
     * @throws IOException of the underlying InputStream if an I/O error occurs.
     */
    public synchronized Optional<String> read() throws IOException {
        int read = _is.read(cbuf, 0, slidingInterval);
        String append;
        if (read == -1) {
            return Optional.empty();
        } else if (read < slidingInterval) {
            append = new String(cbuf, 0, read)
                    + String.join("", Collections.nCopies(slidingInterval - read, " "));
        } else {
            append = new String(cbuf);
        }
        doSlide(append);
        return Optional.of(window);
    }

    private void doSlide(String append) {
        String prefix = window.length() > slidingInterval ? window.substring(slidingInterval) : window;
        window = prefix + append;
    }

    @Override
    public void close() throws IOException {
        _is.close();
    }
}
