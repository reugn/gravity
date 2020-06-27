package com.github.reugn.gravity.util;

/**
 * Pattern filters
 */
public class Filters {

    private Filters() {
    }

    private static final String specialCharsRegex = "[^\\p{L}\\d\\s_]";

    public static String specialChars(String str) {
        return str.replaceAll(specialCharsRegex, "");
    }

    public static String specialCharsToLower(String str) {
        return str.replaceAll(specialCharsRegex, "").toLowerCase();
    }
}
