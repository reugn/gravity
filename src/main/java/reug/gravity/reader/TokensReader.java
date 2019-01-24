package reug.gravity.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TokensReader implements AutoCloseable {

    /**
     * Buffered reader of underlying InputStream
     */
    private BufferedReader reader;

    /**
     * Number of concatenated tokens per slice on read
     */
    private int tokensPerSlice;

    /**
     * Tokens delimiter
     */
    private String delimiter;

    /**
     * Tokens buffer
     */
    private List<String> buffer = new LinkedList<>();

    /**
     * End of stream indicator
     */
    private Boolean eos = false;

    public TokensReader(InputStream is) {
        this(is, 1, " ");
    }

    public TokensReader(InputStream is, int tokensPerSlice) {
        this(is, tokensPerSlice, " ");
    }

    public TokensReader(InputStream is, int tokensPerSlice, String delimiter) {
        reader = new BufferedReader(new InputStreamReader(is));
        this.tokensPerSlice = tokensPerSlice;
        this.delimiter = delimiter;
    }

    /**
     * Reads slices of <code>tokensPerSlice</code> concatenated tokens from underlying InputStream,
     * separated by <code>delimiter</code>.
     * <p>
     * Shifts by one token on each read.
     *
     * @return optional of slice or <code>Optional.empty()<code/> on end of stream.
     * @throws IOException of underlying InputStream
     */
    public synchronized Optional<String> read() throws IOException {
        refill();
        if (buffer.isEmpty()) {
            return Optional.empty();
        } else {
            int to = Math.min(buffer.size(), tokensPerSlice);
            String slice = buffer.subList(0, to).stream().collect(Collectors.joining(delimiter));
            buffer.remove(0);
            return Optional.of(slice);
        }
    }

    private void refill() throws IOException {
        if (!eos && buffer.size() < tokensPerSlice) {
            String line;
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    if (line.trim().length() == 0) continue;
                    buffer.addAll(Arrays.stream(line.trim().split(delimiter))
                            .filter(t -> (!t.equals(delimiter) && !t.isEmpty()))
                            .collect(Collectors.toList()));
                } else eos = true;
                break;
            }
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
