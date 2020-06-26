package com.github.reugn.gravity.util;

import com.github.reugn.gravity.model.Pattern;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return new BufferedReader(new InputStreamReader(is)).lines().map(line -> {
            List<String> tokens = Arrays.asList(line.split(","));
            return toPattern(tokens, filter);
        }).collect(Collectors.toList());
    }

    public static List<Pattern> fromCSV(String in, Function<String, String> filter) {
        return fromCSV(new ByteArrayInputStream(in.getBytes()), filter);
    }

    private static Pattern toPattern(List<String> tokens, Function<String, String> filter) {
        Pattern p;
        try {
            p = new Pattern(
                    tokens.get(0),
                    tokens.size() > 1 ? Integer.parseInt(tokens.get(1)) : 1,
                    tokens.size() > 2 ? Integer.parseInt(tokens.get(2)) : 1,
                    filter
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid csv format", e);
        }
        return p;
    }
}
