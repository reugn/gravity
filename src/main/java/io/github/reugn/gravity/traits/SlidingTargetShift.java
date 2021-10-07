package io.github.reugn.gravity.traits;

import io.github.reugn.gravity.model.Pattern;

@FunctionalInterface
public interface SlidingTargetShift {

    String shiftTarget(Pattern p, String target);

    SlidingTargetShift doShift = (Pattern p, String target) -> {
        int beginIndex = Math.max((target.length() / 2) - (p.getSearch().length() - 1), 0);
        return target.substring(beginIndex).trim();
    };

    SlidingTargetShift identity = (Pattern p, String target) -> target;
}
