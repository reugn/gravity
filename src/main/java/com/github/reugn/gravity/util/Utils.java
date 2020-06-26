package com.github.reugn.gravity.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utils {

    private Utils() {
    }

    public static String toStr(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines()
                .parallel().collect(Collectors.joining(System.lineSeparator()));
    }

    public static InputStream readResource(String fileName) {
        return Utils.class.getResourceAsStream(fileName);
    }
}
