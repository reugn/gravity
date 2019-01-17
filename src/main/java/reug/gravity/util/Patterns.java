package reug.gravity.util;

import reug.gravity.model.Pattern;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Patterns {

    private Patterns() {
    }

    public static List<Pattern> fromCSV(InputStream is) {
        return fromCSV(is, Function.identity());
    }

    public static List<Pattern> fromCSV(String in) {
        return fromCSV(in, Function.identity());
    }

    public static List<Pattern> fromCSV(InputStream is, Function<String, String> filter) {
        List<Pattern> res = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = reader.readLine();
            while (line != null) {
                List<String> tokens = Arrays.asList(line.split(","));
                res.add(toPattern(tokens, filter));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read from csv", e);
        }
        return res;
    }

    public static List<Pattern> fromCSV(String in, Function<String, String> filter) {
        return fromCSV(new ByteArrayInputStream(in.getBytes()), filter);
    }

    private static Pattern toPattern(List<String> tokens, Function<String, String> filter) {
        Pattern p;
        try {
            p = new Pattern(
                    tokens.get(0),
                    tokens.size() > 1 ? Integer.valueOf(tokens.get(1)) : 1,
                    tokens.size() > 2 ? Integer.valueOf(tokens.get(2)) : 1,
                    filter
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid csv format", e);
        }
        return p;
    }
}
