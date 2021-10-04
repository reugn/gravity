package io.github.reugn.gravity.matcher.multi.trie;

import io.github.reugn.gravity.model.Pattern;

import java.util.List;
import java.util.Optional;

public class Trie {

    private final TrieNode root = new TrieNode();

    public Trie() {
    }

    public Trie(List<Pattern> patterns) {
        for (Pattern p : patterns) {
            insert(p);
        }
    }

    public void insert(Pattern pattern) {
        TrieNode current = root;
        String search = pattern.getSearch();
        for (int i = 0; i < search.length(); i++) {
            current = current.getChildren()
                    .computeIfAbsent(search.charAt(i), c -> new TrieNode());
        }
        current.setScore(pattern.getScore(), pattern.getMax());
    }

    public Optional<Integer> match(String slice) {
        TrieNode current = root;
        for (int i = 0; i < slice.length(); i++) {
            char ch = slice.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return Optional.empty();
            }
            current = node;
        }
        return current.incrementAndGetScore();
    }
}
