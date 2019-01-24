package reug.gravity.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

public class SlidingReader implements AutoCloseable {

    /**
     * Underlying Input stream
     */
    private InputStream _is;

    /**
     * Sliding interval size
     */
    private int slidingInterval;

    /**
     * Stream reading buffer
     */
    private byte[] cbuf;

    /**
     * Actual sliding window object.
     * Has <code>slidingInterval * 2<code/> length and slides by <code>slidingInterval<code/>
     */
    private String window;

    public SlidingReader(InputStream is, int sliding_interval) {
        _is = new BufferedInputStream(is);
        slidingInterval = sliding_interval;
        cbuf = new byte[sliding_interval];
        window = String.join("", Collections.nCopies(sliding_interval, " "));
    }

    /**
     * Read from underlying InputStream and return sliding window
     * <p>
     * (previous) aaaaabbbbb
     * <p>
     * (read) ccccc
     * <p>
     * (return) bbbbbccccc
     *
     * @return optional of sliding window or <code>Optional.empty()<code/> on end of stream.
     * @throws IOException of underlying InputStream
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
