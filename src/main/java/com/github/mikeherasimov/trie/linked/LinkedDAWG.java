package com.github.mikeherasimov.trie.linked;

import com.github.mikeherasimov.trie.DAWG;

/**
 * LinkedDAWG represents optimized LinkedTrie data-structure.
 */
public final class LinkedDAWG implements DAWG {
    private LinkedTrie trie;

    LinkedDAWG(LinkedTrie trie){
        this.trie = trie;
    }

    @Override
    public boolean contains(String word) {
        return trie.contains(word);
    }

    @Override
    public boolean isPrefix(String prefix) {
        return trie.isPrefix(prefix);
    }

    @Override
    public int size() {
        return trie.size();
    }
}
