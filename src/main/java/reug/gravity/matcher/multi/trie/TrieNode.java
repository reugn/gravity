package reug.gravity.matcher.multi.trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class TrieNode {

    private final Map<Character, TrieNode> children = new HashMap<>();
    private int score = 0;
    private int maxSpot = 0;
    private int hit = 0;

    Map<Character, TrieNode> getChildren() {
        return children;
    }

    void setScore(int score, int maxSpot) {
        this.score = score;
        this.maxSpot = maxSpot;
    }

    Optional<Integer> getScore() {
        return Optional.of(score);
    }

    synchronized Optional<Integer> incrementAndGetScore() {
        if (score > 0 && hit++ < maxSpot)
            return Optional.of(score);
        else
            return Optional.empty();
    }
}
