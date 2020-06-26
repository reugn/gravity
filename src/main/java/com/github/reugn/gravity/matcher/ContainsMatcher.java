package com.github.reugn.gravity.matcher;

public class ContainsMatcher extends BaseMatcher {

    @Override
    public int doMatch(String pattern, String target) {
        int index = target.indexOf(pattern);
        if (index == -1)
            return NOT_FOUND;
        else
            return 1 + doMatch(pattern, target.substring(index + pattern.length()));
    }
}
